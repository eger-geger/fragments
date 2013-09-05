package com.astound.fragments.pages;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class WikiPage extends BasePage {

    public WikiPage(WebDriver webDriver) {
        super(webDriver);
    }

    @FindBy(id = "p-logo")
    private Fragment homeLink;

    public void openHomePage() {
        homeLink.click();
    }

    public boolean isValidPage() {
        return homeLink.isPresent();
    }
}
