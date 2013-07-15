package com.astound.fragments.locators;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;


public class DynamicLocatorFactory implements ElementLocatorFactory {

    private final SearchContext searchContext;

    public DynamicLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    public ElementLocator createLocator(Field field) {
        return new DynamicElementLocator(searchContext, field);
    }
}
