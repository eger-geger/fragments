package com.astound.fragments.locators.parsers;

import org.openqa.selenium.By;

public interface LocatorParser<T> {

    public By parse(T t);

}
