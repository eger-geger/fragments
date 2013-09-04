package com.astound.fragments.locators.extractors;

import com.astound.fragments.locators.parsers.LocatorParser;
import com.google.common.base.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FromFindBys implements ByExtractor {

    private final LocatorParser<FindBy> findByParser;

    public FromFindBys(LocatorParser<FindBy> findByParser) {
        this.findByParser = findByParser;
    }

    @Override public Optional<By> extract(Field field) {
        FindBys findBysAnnotation = field.getAnnotation(FindBys.class);

        return isApplicableFindBys(findBysAnnotation)
                ? Optional.of(chainedBy(findBysAnnotation.value()))
                : Optional.<By>absent();
    }

    private By chainedBy(FindBy[] findByArray) {
        ChainedBy chainedBy = new ChainedBy(findByParser.parse(findByArray[0]));

        for (int i = 1; i < findByArray.length; i++) {
            chainedBy.addToChain(findByParser.parse(findByArray[i]));
        }

        return chainedBy;
    }

    private static boolean isApplicableFindBys(FindBys findBys) {
        return findBys != null && findBys.value().length > 0;
    }

    private static class ChainedBy extends By {

        private final By wrappedBy;

        private ChainedBy nextInChain;

        private ChainedBy(By wrappedBy) {
            this.wrappedBy = wrappedBy;
        }

        @Override public List<WebElement> findElements(SearchContext searchContext) {
            List<WebElement> elementList = new ArrayList<>();

            for (WebElement webElement : wrappedBy.findElements(searchContext)) {
                elementList.addAll(webElement.findElements(nextInChain));
            }

            return elementList;
        }

        public void addToChain(By by) {
            if (nextInChain == null) {
                nextInChain = new ChainedBy(by);
            } else {
                nextInChain.addToChain(by);
            }
        }
    }
}
