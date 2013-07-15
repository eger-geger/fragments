package com.astound.fragments.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;


public class DynamicElementLocator implements ElementLocator {

    private final SearchContext searchContext;

    private final By by;

    public DynamicElementLocator(SearchContext searchContext, Field field) {
        this.searchContext = searchContext;

        by = ServiceLocatorResolver.getInstance().isApplicable(field)
                ? ServiceLocatorResolver.getInstance().resolve(field)
                : DefaultLocatorResolver.getInstance().resolve(field);
    }

    public WebElement findElement() { return searchContext.findElement(by); }

    public List<WebElement> findElements() { return searchContext.findElements(by); }

    public String toString() { return by.toString(); }

}