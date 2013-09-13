package com.astound.fragments.elements;

import com.astound.fragments.context.FragmentContext;
import com.astound.fragments.events.EventType;
import com.astound.fragments.events.Publish;
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

    @Publish(format = "setting checkbox state to [{1}]", type = EventType.WEB_DRIVER_EVENT)
    public void setState(boolean state) {
        waitUntil(5, state ? IS_CHECKED : IS_NOT_CHECKED);
    }

    public void setChecked() {
        setState(true);
    }

    public void setUnChecked() {
        setState(false);
    }

    @Publish(format = "setting checkbox state with JS to [{1}]", type = EventType.WEB_DRIVER_EVENT)
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
