package com.astound.fragments.locators;

import org.openqa.selenium.By;

import java.lang.reflect.Field;

public interface ILocatorResolver {

    public By resolve(Field field);

    public boolean isApplicable(Field field);

}