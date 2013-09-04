package com.astound.fragments.locators;

import com.astound.fragments.locators.extractors.ByExtractorService;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;

public class FragmentLocator implements ElementLocator {

    private final SearchContext searchContext;

    private final By by;

    public FragmentLocator(SearchContext searchContext, By by) {
        this.searchContext = searchContext;
        this.by = by;
    }

    public FragmentLocator(SearchContext searchContext, Field field) {
        this(searchContext, buildByFromField(field));
    }

    @Override public WebElement findElement() { return searchContext.findElement(by); }

    @Override public List<WebElement> findElements() { return searchContext.findElements(by); }

    @Override public String toString() { return by.toString(); }

    private static By buildByFromField(Field field) {
        return ByExtractorService.getInstance().extract(field).or(new ByNotAvailable(field));
    }
}
