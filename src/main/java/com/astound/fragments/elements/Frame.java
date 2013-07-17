package com.astound.fragments.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.astound.fragments.utils.PageUtils.getParentPage;

public class Frame extends Fragment {

    @Override public WebElement findElement(By by) {
        return getParentPage(this).findElement(by);
    }

    @Override public List<WebElement> findElements(By by) {
        return getParentPage(this).findElements(by);
    }
}
