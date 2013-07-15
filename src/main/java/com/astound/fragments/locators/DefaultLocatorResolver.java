package com.astound.fragments.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.Annotations;

import java.lang.reflect.Field;

public class DefaultLocatorResolver implements ILocatorResolver {

    private static ILocatorResolver instance;

    private DefaultLocatorResolver() {}

    public static ILocatorResolver getInstance() {
        if (instance == null) {
            instance = new DefaultLocatorResolver();
        }
        return instance;
    }

    public boolean isApplicable(Field field) { return true; }

    public By resolve(Field field) {
        return new Annotations(field).buildBy();
    }
}
