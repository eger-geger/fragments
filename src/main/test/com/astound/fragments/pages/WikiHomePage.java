package com.astound.fragments.pages;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

public class WikiHomePage extends WikiPage {

	public WikiHomePage(WebDriver webDriver) {
		super(webDriver);
	}

	@FindBys({@FindBy(id = "p-navigation"), @FindBy(tagName = "a")})
	private List<Fragment> navigationLinks;

	public void openNavigationLink(int index){
		navigationLinks.get(index).click();
	}

	public boolean isValidPage() {
		return navigationLinks.size() > 0;
	}
}
