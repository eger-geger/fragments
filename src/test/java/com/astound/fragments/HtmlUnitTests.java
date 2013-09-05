package com.astound.fragments;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class HtmlUnitTests extends WebDriverTests {

    @Override protected WebDriver initWebDriver() {
        return new HtmlUnitDriver(true);
    }
}
