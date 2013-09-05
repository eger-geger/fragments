package com.astound.fragments;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FireFoxTests extends WebDriverTests {

    @Override protected WebDriver initWebDriver() {
        return new FirefoxDriver();
    }
}
