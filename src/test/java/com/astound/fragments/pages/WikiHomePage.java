package com.astound.fragments.pages;

import com.astound.fragments.elements.Fragment;
import com.astound.fragments.parts.CollapsibleMenu;
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

	@FindBy(id = "p-participation")
	public CollapsibleMenu participationMenu;

	public void openNavigationLink(int index) {
		navigationLinks.get(index).click();
	}

	@Override public boolean isValidPage() {
		return navigationLinks.size() > 0;
	}
}
