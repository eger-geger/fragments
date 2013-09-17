package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebDriver;

/**
 * Extend this class in order to interact with frame of type #T
 *
 * @see Frame
 */
public abstract class FrameHandler<T extends Fragment> {

    private final WebDriver webDriver;

    protected FrameHandler(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /** Perform your actions within #T frame here */
    protected abstract void doPerform(T frame);

    /**
     * Call this in order to perform your interaction
     *
     * @param frame - instance of frame to work with
     */
    public final void perform(T frame) {
        webDriver.switchTo().frame(frame.getWrappedElement());

        doPerform(frame);

        webDriver.switchTo().defaultContent();
    }
}
