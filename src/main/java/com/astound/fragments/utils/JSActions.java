package com.astound.fragments.utils;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.JavascriptExecutor;

import static com.astound.fragments.utils.PageUtils.getParentPage;

public class JSActions {

    public static JavascriptExecutor getJSExecutor(Fragment fragment) {
        return new PageJSExecutor(getParentPage(fragment));
    }

    public static void click(Fragment fragment) {
        getJSExecutor(fragment).executeScript("arguments[0].click()", fragment.getWrappedElement());
    }

    public static String getTextContent(Fragment fragment) {
        return (String) getJSExecutor(fragment).executeScript("return arguments[0].textContent",
                fragment.getWrappedElement());
    }

    public static String getHtml(Fragment fragment) {
        return (String) getJSExecutor(fragment).executeScript("return arguments[0].innerHTML",
                fragment.getWrappedElement());
    }

    public static String getToString(Fragment fragment) {
        return (String) getJSExecutor(fragment).executeScript("return arguments[0].toString()",
                fragment.getWrappedElement());
    }

    public static String getAttribute(Fragment fragment, String name) {
        return (String) getJSExecutor(fragment).executeScript("return arguments[0].getAttribute(arguments[1])",
                fragment.getWrappedElement(), name);
    }

    public static void setAttribute(Fragment fragment, String name, String value) {
        getJSExecutor(fragment).executeScript("arguments[0].setAttribute(arguments[1], arguments[2])",
                fragment.getWrappedElement(), name, value);
    }

    public static void removeAttribute(Fragment fragment, String name) {
        getJSExecutor(fragment).executeScript("arguments[0].removeAttribute(arguments[1])",
                fragment.getWrappedElement(), name);
    }

    public static String getProperty(Fragment fragment, String propertyName) {
        return (String) getJSExecutor(fragment).executeScript("return arguments[0]." + propertyName, fragment);
    }

}
