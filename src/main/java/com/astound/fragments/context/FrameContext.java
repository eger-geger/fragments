package com.astound.fragments.context;

import com.astound.fragments.FragmentFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FrameContext extends DefaultContext {

    private final WebElement frameWebElement;

    public FrameContext(WebElement frameWebElement, WebDriver webDriver, FragmentFactory fragmentFactory, String contextName) {
        super(webDriver, (JavascriptExecutor) webDriver, fragmentFactory, contextName);

        this.frameWebElement = frameWebElement;
    }

    @Override public WebElement getRootElement() {
        return frameWebElement;
    }
}
