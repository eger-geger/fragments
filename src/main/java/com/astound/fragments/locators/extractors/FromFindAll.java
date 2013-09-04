package com.astound.fragments.locators.extractors;

import com.astound.fragments.locators.parsers.LocatorParser;
import com.google.common.base.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FromFindAll implements ByExtractor {

    private final LocatorParser<FindBy> findByParser;

    public FromFindAll(LocatorParser<FindBy> findByParser) {
        this.findByParser = findByParser;
    }

    @Override public Optional<By> extract(Field field) {
        FindAll findAllAnnotation = field.getAnnotation(FindAll.class);

        return isApplicable(findAllAnnotation)
                ? Optional.of(buildAdditiveBy(findAllAnnotation.value()))
                : Optional.<By>absent();
    }

    private By buildAdditiveBy(FindBy[] findByArr) {
        AdditiveBy additiveBy = new AdditiveBy();

        for (FindBy findBy : findByArr) {
            additiveBy.addBy(findByParser.parse(findBy));
        }

        return additiveBy;
    }

    private static boolean isApplicable(FindAll findAllAnnotation) {
        return findAllAnnotation != null && findAllAnnotation.value().length > 0;
    }

    private static class AdditiveBy extends By {

        private final List<By> byList = new LinkedList<>();

        @Override public List<WebElement> findElements(SearchContext searchContext) {
            List<WebElement> elementList = new ArrayList<>();

            for (By by : byList) {
                elementList.addAll(by.findElements(searchContext));
            }

            return elementList;
        }

        public void addBy(By by) {
            byList.add(by);
        }
    }

}
