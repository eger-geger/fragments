package com.astound.fragments.proxy;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

class ElementCache {

    private WebElement cached;

    private final ElementLocator locator;

    ElementCache(ElementLocator locator) {
        this.locator = locator;
    }

    public WebElement getElement() {
        if (cached == null) {
            cached = locator.findElement();
        }

        invalidateCache();

        return cached;
    }

    private void invalidateCache() {
        try {
            cached.getTagName();
        } catch (StaleElementReferenceException ex) {
            cached = locator.findElement();
        }
    }

}
