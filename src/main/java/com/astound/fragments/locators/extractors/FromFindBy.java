package com.astound.fragments.locators.extractors;

import com.astound.fragments.locators.parsers.LocatorParser;
import com.google.common.base.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.lang.reflect.Field;

public class FromFindBy implements ByExtractor {

    private final LocatorParser<FindBy> findByParser;

    public FromFindBy(LocatorParser<FindBy> findByParser) {
        this.findByParser = findByParser;
    }

    @Override public Optional<By> extract(Field field) {
        FindBy findByAnnotation = field.getAnnotation(FindBy.class);

        return findByAnnotation == null
                ? Optional.<By>absent()
                : Optional.of(findByParser.parse(findByAnnotation));
    }
}
