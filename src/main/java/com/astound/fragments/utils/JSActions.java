package com.astound.fragments.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

public class JSActions {

    private final JavascriptExecutor jsExecutor;

    public JSActions(JavascriptExecutor jsExecutor) {
        this.jsExecutor = jsExecutor;
    }

    public void click(WebElement webElement) {
        jsExecutor.executeScript("arguments[0].click()", webElement);
    }

    public String getTextContent(WebElement webElement) {
        return (String) jsExecutor.executeScript("return arguments[0].textContent", webElement);
    }

    public String getHtml(WebElement webElement) {
        return (String) jsExecutor.executeScript("return arguments[0].innerHTML", webElement);
    }

    public String toString(WebElement webElement) {
        return (String) jsExecutor.executeScript("return arguments[0].toString()", webElement);
    }

    public String getAttribute(WebElement webElement, String name) {
        return (String) jsExecutor.executeScript("return arguments[0].getAttribute(arguments[1])", webElement, name);
    }

    public void setAttribute(WebElement webElement, String name, String value) {
        jsExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", webElement, name, value);
    }

    public void removeAttribute(WebElement webElement, String name) {
        jsExecutor.executeScript("arguments[0].removeAttribute(arguments[1])", webElement, name);
    }

    public String getProperty(WebElement webElement, String propertyName) {
        return (String) jsExecutor.executeScript("return arguments[0]." + propertyName, webElement);
    }

    public void setProperty(WebElement webElement, String property, Object value) {
        jsExecutor.executeScript(String.format("arguments[0].[%s] = arguments[1]", property), webElement, value);
    }

}
