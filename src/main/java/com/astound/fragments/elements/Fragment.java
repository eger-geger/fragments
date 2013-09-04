package com.astound.fragments.elements;

import com.astound.fragments.PageContext;
import com.astound.fragments.events.PublishEvent;
import com.astound.fragments.utils.JSActions;
import com.google.common.base.Predicate;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.astound.fragments.utils.XPathFunctions.containstext;

public class Fragment implements WebElement, WrapsElement, PageContext {

    private static final Predicate<WebElement> IS_VISIBLE = new Predicate<WebElement>() {
        @Override public boolean apply(WebElement input) { return input.isDisplayed(); }
    };

    private static final Predicate<WebElement> IS_HIDDEN = new Predicate<WebElement>() {
        @Override public boolean apply(WebElement input) { return !input.isDisplayed(); }
    };

    private final PageContext wrappedContext;

    private final WebElement wrappedElement;

    protected final JSActions jsActions;

    public Fragment(PageContext pageContext) {
        wrappedContext = pageContext;
        wrappedElement = pageContext.findElement(By.xpath("."));
        jsActions = new JSActions(this);
    }

    @Override public String getName() {return wrappedContext.getName();}

    @Override public Fragment findFragment(By by) {return wrappedContext.findFragment(by);}

    @Override public List<Fragment> findFragments(By by) {return wrappedContext.findFragments(by);}

    @Override public <E extends Fragment> E findFragment(By by, Class<E> aClass) {return wrappedContext.findFragment(by, aClass);}

    @Override public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass) {return wrappedContext.findFragments(by, aClass);}

    @Override public <E extends Fragment> E findFragment(By by, Class<E> aClass, String name) {
        return wrappedContext.findFragment(by, aClass, name);
    }

    @Override public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass, String name) {
        return wrappedContext.findFragments(by, aClass, name);
    }

    @Override public List<WebElement> findElements(By by) {return wrappedContext.findElements(by);}

    @Override public WebElement findElement(By by) {return wrappedContext.findElement(by);}

    @Override public Object executeScript(String script, Object... args) {return wrappedContext.executeScript(script, args);}

    @Override public Object executeAsyncScript(String script, Object... args) {return wrappedContext.executeAsyncScript(script, args);}

    public String getHtml() { return jsActions.getHtml(getWrappedElement()); }

    public String getTextContent() { return jsActions.getTextContent(getWrappedElement()); }

    public void setAttribute(String attributeName, String attributeValue) {
        publishEvent(String.format("setting attribute [%s] with [%s]", attributeName, attributeValue));
        jsActions.setAttribute(getWrappedElement(), attributeName, attributeValue);
    }

    public void removeAttribute(String attributeName) {
        publishEvent(String.format("removing attribute [%s]", attributeName));
        jsActions.removeAttribute(getWrappedElement(), attributeName);
    }

    @Override public String getAttribute(String attributeName) { return jsActions.getAttribute(getWrappedElement(), attributeName); }

    public String getProperty(String propertyName) { return jsActions.getProperty(getWrappedElement(), propertyName); }

    public void setProperty(String property, Object value) {
        jsActions.setProperty(getWrappedElement(), property, value);
    }

	@PublishEvent("click on")
    @Override public void click() {
        publishEvent("click");
        wrappedElement.click();
    }

    public void jsClick() {
        publishEvent("click with JS");
        jsActions.click(getWrappedElement());
    }

    @Override public void submit() { wrappedElement.submit(); }

    @Override public void sendKeys(CharSequence... keysToSend) { wrappedElement.sendKeys(keysToSend); }

    @Override public void clear() {
        sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        wrappedElement.clear();
    }

    @Override public String getTagName() { return wrappedElement.getTagName(); }

    @Override public boolean isSelected() { return wrappedElement.isSelected(); }

    @Override public boolean isEnabled() { return wrappedElement.isEnabled(); }

    @Override public String getText() { return wrappedElement.getText(); }

    public boolean isPresent() {
        publishEvent("checking if element is present");

        try {
            wrappedElement.getLocation();
        } catch (WebDriverException ex) {
            return false;
        }

        return true;
    }

    @Override public boolean isDisplayed() {
        boolean isDisplayed = isPresent() && wrappedElement.isDisplayed();

        publishEvent(String.format("Element [%s] is [%s]", getName(), isDisplayed ? "visible" : "hidden"));

        return isDisplayed;
    }

    @Override public Point getLocation() {
        return wrappedElement.getLocation();
    }

    @Override public Dimension getSize() {
        return wrappedElement.getSize();
    }

    @Override public String getCssValue(String propertyName) {
        return wrappedElement.getCssValue(propertyName);
    }

    @Override public WebElement getWrappedElement() {
        return wrappedElement instanceof WrapsElement ? ((WrapsElement) wrappedElement).getWrappedElement() : wrappedElement;
    }

    public void waitUntil(int timeoutInSeconds, Predicate<WebElement> condition) {
        new FluentWait<WebElement>(this)
                .pollingEvery(1, TimeUnit.SECONDS)
                .withTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .withMessage(String.format("[%s] failed", condition))
                .ignoring(WebDriverException.class)
                .until(condition);
    }

    public void waitForHide(int seconds) {
        publishEvent(String.format("Wait for [%s] until element [%s] became hidden", seconds));

        waitUntil(seconds, IS_HIDDEN);
    }

    public void waitForVisible(int seconds) {
        publishEvent(String.format("Wait for [%s] seconds until element [%s] became visible", seconds, getName()));

        waitUntil(seconds, IS_VISIBLE);
    }

    public boolean isElementDisplayed(By by) {
        for (WebElement element : findElements(by)) {
            if (element.isDisplayed()) { return true; }
        }
        return false;
    }

    public boolean isTextPresent(String text) {
        return getHtml().contains(text);
    }

    public boolean isTextDisplayed(String text) {
        return isTextPresent(text) && isElementDisplayed(By
                .xpath(String.format(".//*[%s]", containstext("text()", text))));
    }

    public boolean isValueDisplayed(String value) {
        return isTextPresent(value) && isElementDisplayed(By
                .xpath(String.format(".//*[%s]", containstext("@value", value))));
    }

    protected void publishEvent(String description) {
//        PageEventBus.getEventBus().post(new PageEvent(getName(), description));
    }

    @Override public String toString() {
        return jsActions.toString(getWrappedElement());
    }

}