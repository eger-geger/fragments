package com.astound.fragments.proxy;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

class ListItemLocator implements ElementLocator {

    private final int index;

    private final ElementLocator locator;

    ListItemLocator(int index, ElementLocator locator) {
        this.index = index;
        this.locator = locator;
    }

    @Override public WebElement findElement() {
        try {
            return locator.findElements().get(index);
        } catch (IndexOutOfBoundsException ex) {
            throw new NoSuchElementException(String.format("List item [%s] not found!", index));
        }
    }

    @Override public List<WebElement> findElements() {
        throw new UnsupportedOperationException("Should not be used for ListItemLocator");
    }

    @Override public String toString() {
        return locator.toString();
    }

}
