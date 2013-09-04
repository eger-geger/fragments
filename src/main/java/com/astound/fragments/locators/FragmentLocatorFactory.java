package com.astound.fragments.locators;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;


public class FragmentLocatorFactory implements ElementLocatorFactory {

    private final SearchContext searchContext;

    public FragmentLocatorFactory(SearchContext searchContext) {
        this.searchContext = searchContext;
    }

    @Override public ElementLocator createLocator(Field field) {
        return new FragmentLocator(searchContext, field);
    }
}
