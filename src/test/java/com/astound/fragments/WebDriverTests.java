package com.astound.fragments;

import com.astound.fragments.events.EventBusService;
import com.astound.fragments.pages.BasePage;
import com.astound.fragments.utils.PageEventLogger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.util.concurrent.TimeUnit;

public abstract class WebDriverTests {

    private static final String BASE_WIKI_URL = "http://ru.wikipedia.org";

    private WebDriver webDriver;

    @BeforeSuite public void initEventLogger() {
        EventBusService.getInstance().get().register(new PageEventLogger());
    }

    @BeforeClass public void openBrowser() {
        webDriver = initWebDriver();
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
    }

    @AfterClass public void closeBrowser() {
        webDriver.quit();
    }

    protected abstract WebDriver initWebDriver();

    protected void navigateToWiki() {
        navigateTo(BASE_WIKI_URL);
    }

    protected void navigateTo(String url) {
        webDriver.navigate().to(url);
    }

    protected <P extends BasePage> P createPage(Class<P> pClass) throws Exception {
        return pClass.getConstructor(WebDriver.class).newInstance(webDriver);
    }

}
