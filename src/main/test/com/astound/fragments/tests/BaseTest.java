package com.astound.fragments.tests;

import com.astound.fragments.pages.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.concurrent.TimeUnit;

public class BaseTest {

	private static final String BASE_WIKI_URL = "http://ru.wikipedia.org";

	private WebDriver webDriver;

	@BeforeClass public void openBrowser(){
		webDriver = new FirefoxDriver();
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		webDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
	}

	@AfterClass public void closeBrowser(){
		webDriver.quit();
	}

	protected void navigateToWiki(){
		navigateTo(BASE_WIKI_URL);
	}

	protected void navigateTo(String url){
		webDriver.navigate().to(url);
	}

	protected <P extends BasePage> P createPage(Class<P> pClass) throws Exception {
		return pClass.getConstructor(WebDriver.class).newInstance(webDriver);
	}

}
