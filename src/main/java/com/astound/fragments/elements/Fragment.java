package com.astound.fragments.elements;

import com.astound.fragments.FragmentFactory;
import com.astound.fragments.FragmentSettings;
import com.astound.fragments.IFragmentContext;
import com.astound.fragments.PageWithFragments;
import com.astound.fragments.events.FragmentEvent;
import com.astound.fragments.events.FragmentEventBus;
import com.astound.fragments.locators.DefaultElementLocator;
import com.astound.fragments.utils.JSActions;
import com.astound.fragments.utils.PageUtils;
import com.google.common.base.Predicate;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.astound.fragments.utils.XPathFunctions.containstext;

public class Fragment<Parent extends IFragmentContext> implements WebElement, WrapsElement, IFragmentContext {

    /**
     * Injected with {@link com.astound.fragments.FragmentFactory}
     */
    protected final FragmentFactory fragmentFactory = null;

    /**
     * Injected with {@link FragmentFactory}
     */
    private final WebElement wrapped = null;

    /**
     * Injected with {@link FragmentFactory}
     */
    private final Parent parent = null;

    /**
     * Injected with {@link FragmentFactory}
     */
    private final String locator = null;

    /**
     * Injected with {@link FragmentFactory}
     */
    private final String contextName = null;

    public String getName() { return contextName; }

    public WebElement findElement(By by) {
        return wrapped.findElement(by);
    }

    public List<WebElement> findElements(By by) {
        return wrapped.findElements(by);
    }

    public Fragment findFragment(By by) {
        return findFragment(by, Fragment.class);
    }

    public <E extends Fragment<?>> E findFragment(By by, Class<E> type) {
        return findFragment(by, type, by.toString());
    }

    public <E extends Fragment<?>> E findFragment(By by, Class<E> type, String name) {
        return fragmentFactory.createFragment(type, new DefaultElementLocator(wrapped, by), name);
    }

    public List<Fragment> findFragments(By by) {
        return findFragments(by, Fragment.class);
    }

    public <E extends Fragment<?>> List<E> findFragments(By by, Class<E> type) {
        return findFragments(by, type, by.toString());
    }

    public <E extends Fragment<?>> List<E> findFragments(By by, Class<E> type, String name) {
        return fragmentFactory.createList(type, new DefaultElementLocator(wrapped, by), name);
    }

    public Parent getParent() { return parent; }

    public String getLocator() { return locator; }

    public String getHtml() { return JSActions.getHtml(this); }

    public String getTextContent() { return JSActions.getTextContent(this); }

    public void setAttribute(String attributeName, String attributeValue) {
        publishEvent(String.format("setting attribute [%s] with [%s]", attributeName, attributeValue));
        JSActions.setAttribute(this, attributeName, attributeValue);
    }

    public void removeAttribute(String attributeName) {
        publishEvent(String.format("removing attribute [%s]", attributeName));
        JSActions.removeAttribute(this, attributeName);
    }

    public String getAttribute(String attributeName) { return JSActions.getAttribute(this, attributeName); }

    public String getProperty(String propertyName) { return JSActions.getProperty(this, propertyName); }

    public void click() {
        publishEvent("click");
        wrapped.click();
    }

    public void jsClick() {
        publishEvent("click with JS");
        JSActions.click(this);
    }

    public void submit() { wrapped.submit(); }

    public void sendKeys(CharSequence... keysToSend) { wrapped.sendKeys(keysToSend); }

    public void clear() {
        sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        wrapped.clear();
    }

    public String getTagName() { return wrapped.getTagName(); }

    public boolean isSelected() { return wrapped.isSelected(); }

    public boolean isEnabled() { return wrapped.isEnabled(); }

    public String getText() { return wrapped.getText(); }

    public boolean isPresent() {
        publishEvent("checking if element is present");

        try {
            wrapped.getLocation();
        } catch (WebDriverException ex) {
            return false;
        }

        return true;
    }

    public boolean isDisplayed() {
        publishEvent("checking if element is visible");
        return isPresent() && wrapped.isDisplayed();
    }

    public Point getLocation() { return wrapped.getLocation(); }

    public Dimension getSize() { return wrapped.getSize(); }

    public String getCssValue(String propertyName) { return wrapped.getCssValue(propertyName); }

    public WebElement getWrappedElement() {
        return wrapped instanceof WrapsElement ? ((WrapsElement) wrapped).getWrappedElement() : wrapped;
    }

    public void waitForHide(int timeout) {
        publishEvent(String.format("wait until hide for [%s] seconds", timeout));

        PageWithFragments parentPage = PageUtils.getParentPage(this);

        parentPage.setImplicitWait(500, TimeUnit.MILLISECONDS);

        new FluentWait<WebElement>(this)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .ignoring(WebDriverException.class)
                .until(new Predicate<WebElement>() {

                    public boolean apply(WebElement element) {
                        return !element.isDisplayed();
                    }

                });

        parentPage.setImplicitWait(FragmentSettings.getInstance().getImplicitWait(), TimeUnit.MILLISECONDS);
    }

    public void waitForVisible(int timeout) {
        publishEvent(String.format("wait until visible for [%s] seconds", timeout));

        PageWithFragments parentPage = PageUtils.getParentPage(this);

        parentPage.setImplicitWait(500, TimeUnit.MILLISECONDS);

        new FluentWait<WebElement>(this)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .withTimeout(timeout, TimeUnit.SECONDS)
                .ignoring(WebDriverException.class)
                .until(new Predicate<WebElement>() {
                    public boolean apply(WebElement element) {
                        return element.isDisplayed();
                    }
                });

        parentPage.setImplicitWait(FragmentSettings.getInstance().getImplicitWait(), TimeUnit.MILLISECONDS);
    }

    public boolean isElementDisplayed(By by) {
        for (WebElement element : findElements(by)) {
            if (element.isDisplayed()) { return true; }
        }
        return false;
    }

    public boolean isTextPresent(String text) { return getHtml().contains(text); }

    public boolean isTextDisplayed(String text) {
        return isTextPresent(text) && isElementDisplayed(By
                .xpath(String.format(".//*[%s]", containstext("text()", text))));
    }

    public boolean isValueDisplayed(String value) {
        return isTextPresent(value) && isElementDisplayed(By
                .xpath(String.format(".//*[%s]", containstext("@value", value))));
    }

    protected void publishEvent(String description) {
        FragmentEventBus.getEventBus().post(new FragmentEvent(this, description));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getName());

        IFragmentContext context = this.parent;

        while (context instanceof Fragment) {
            sb.insert(0, context.getName() + '.');
            context = ((Fragment) context).getParent();
        }

        return sb.toString();
    }

}