package com.astound.fragments.elements;

import com.astound.fragments.FragmentContext;
import com.google.common.base.Predicate;
import org.openqa.selenium.WebElement;

public class CheckBox extends Fragment {

	private static final String JS_PROPERTY_CHECKED = "checked";

	private static final Predicate<WebElement> IS_CHECKED = new Predicate<WebElement>() {
		@Override public boolean apply(WebElement input) {
			if (input.isSelected()) { return true; }
			input.click();
			return input.isSelected();
		}
	};

	private static final Predicate<WebElement> IS_NOT_CHECKED = new Predicate<WebElement>() {
		@Override public boolean apply(WebElement input) {
			if (!input.isSelected()) { return true; }
			input.click();
			return !input.isSelected();
		}
	};

	public CheckBox(FragmentContext fragmentContext) {
		super(fragmentContext);
	}

	public void setState(boolean state) {
		waitUntil(5, state ? IS_CHECKED : IS_NOT_CHECKED);
	}

	public void setChecked() {
		setState(true);
	}

	public void setUnChecked() {
		setState(false);
	}

	public void setStateWithJS(boolean state) {
		setProperty(JS_PROPERTY_CHECKED, state);
	}

	public void setCheckedWithJS() {
		setStateWithJS(true);
	}

	public void setUnCheckedWithJS() {
		setStateWithJS(false);
	}
}
