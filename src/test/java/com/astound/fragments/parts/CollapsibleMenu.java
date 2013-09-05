package com.astound.fragments.parts;

import com.astound.fragments.FragmentContext;
import com.astound.fragments.elements.Fragment;
import com.google.common.base.Predicate;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CollapsibleMenu extends Fragment {

	public CollapsibleMenu(FragmentContext fragmentContext) {
		super(fragmentContext);
	}

	@FindBy(tagName = "h3")
	private Fragment headerToggle;

	@FindBy(css = ".body li a")
	private List<Fragment> links;

	public void openLink(int index) {
		links.get(index).click();
	}

	public int getLinkCount() {
		return links.size();
	}

	public void open() {
		if (isClosed()) {
			headerToggle.jsClick();

			waitUntil(5, new Predicate<WebElement>() {
				@Override public boolean apply(WebElement input) { return isOpened(); }
			});
		}
	}

	public void hide() {
		if (isOpened()) {
			headerToggle.jsClick();

			waitUntil(5, new Predicate<WebElement>() {
				@Override public boolean apply(WebElement input) { return isClosed(); }
			});
		}
	}

	public boolean isOpened() {
		return links.get(0).isDisplayed();
	}

	public boolean isClosed() {
		return !isOpened();
	}
}
