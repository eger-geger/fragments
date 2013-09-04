package com.astound.fragments.locators.extractors;

import com.astound.fragments.locators.FindByString;
import com.astound.fragments.locators.parsers.LocatorParser;
import com.google.common.base.Optional;
import org.openqa.selenium.By;

import java.lang.reflect.Field;

public abstract class FromFindByString implements ByExtractor {

    private final LocatorParser<FindByString> locatorParser;

    protected FromFindByString(LocatorParser<FindByString> locatorParser) {
        this.locatorParser = locatorParser;
    }

    @Override public Optional<By> extract(Field field) {
        FindByString findByString = field.getAnnotation(FindByString.class);

        return isApplicable(findByString)
                ? Optional.of(locatorParser.parse(findByString))
                : Optional.<By>absent();
    }

    protected abstract boolean isApplicable(FindByString findByString);

}
