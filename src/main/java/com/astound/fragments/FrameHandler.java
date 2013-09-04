package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebDriver;

public abstract class FrameHandler<T extends Fragment> {

    private final WebDriver webDriver;

    protected FrameHandler(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    protected abstract void doPerform(T frame);

    public void perform(T frame) {
        webDriver.switchTo().frame(frame.getWrappedElement());

        doPerform(frame);

        webDriver.switchTo().defaultContent();
    }
}
