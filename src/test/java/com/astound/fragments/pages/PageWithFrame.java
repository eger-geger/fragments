package com.astound.fragments.pages;

import com.astound.fragments.Frame;
import com.astound.fragments.FrameHandler;
import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class PageWithFrame extends BasePage {

    @FindBy(id = "frameId")
    @Frame private Fragment frameFragment;

    public PageWithFrame(WebDriver webDriver) {
        super(webDriver);
    }

    public void doWithLocalFrameHandler() {
        new LocalFrameHandler<Fragment>() {
            @Override protected void doPerform(Fragment frame) {
                //Perform some actions within frame here
            }
        }.perform(frameFragment);   // And provide frame
    }

    public void doWithFrameHandler() {
        new FrameHandler<Fragment>(webDriver) {
            @Override protected void doPerform(Fragment frame) {
                //Perform some actions within frame here.
            }
        }.perform(frameFragment);   //And provide frame
    }

}
