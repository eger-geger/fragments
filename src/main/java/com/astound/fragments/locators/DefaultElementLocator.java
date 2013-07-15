package com.astound.fragments.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

public class DefaultElementLocator implements ElementLocator {

    private final SearchContext searchContext;

    private final By by;

    public DefaultElementLocator(SearchContext searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    public WebElement findElement() {
        return searchContext.findElement(by);
    }

    public List<WebElement> findElements() {
        return searchContext.findElements(by);
    }

    public String toString() {
        return by.toString();
    }
}
