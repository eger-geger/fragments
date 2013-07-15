package com.astound.fragments.utils;

import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.JavascriptExecutor;

public class PageJSExecutor implements JavascriptExecutor {

    private final PageObject pageObject;

    public PageJSExecutor(PageObject pageObject) {
        this.pageObject = pageObject;
    }

    public Object executeAsyncScript(String script, Object... args) {
        throw new NoSuchMethodError("PageObjects cannot execute javascript asycnchroniously.");
    }

    public Object executeScript(String script, Object... args) {
        return pageObject.evaluateJavascript(script, args);
    }
}
