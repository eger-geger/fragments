package com.astound.fragments.elements;

import com.astound.fragments.PageContext;
import com.astound.fragments.events.Publish;
import com.astound.fragments.proxy.ElementLazyLoader;
import com.astound.fragments.utils.JSActions;
import com.google.common.base.Predicate;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.InvocationHandler;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.astound.fragments.utils.XPathFunctions.containstext;
import static java.lang.reflect.Proxy.newProxyInstance;

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
        wrappedElement = lazyLoadedElement(pageContext);
        jsActions = new JSActions(this);
    }

    private static WebElement lazyLoadedElement(final PageContext pageContext) {
        ClassLoader classLoader = pageContext.getClass().getClassLoader();

        InvocationHandler invocationHandler = new ElementLazyLoader(new ElementLocator() {
            @Override public WebElement findElement() { return pageContext.findElement(By.xpath(".")); }

            @Override public List<WebElement> findElements() { return Arrays.asList(findElement()); }
        });

        return (WebElement) newProxyInstance(classLoader, new Class[]{WebElement.class, WrapsElement.class}, invocationHandler);
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

    @Publish
    public String getHtml() { return jsActions.getHtml(getWrappedElement()); }

    @Publish
    public String getTextContent() { return jsActions.getTextContent(getWrappedElement()); }

    @Publish
    public void setAttribute(String attributeName, String attributeValue) {
        jsActions.setAttribute(getWrappedElement(), attributeName, attributeValue);
    }

    @Publish
    public void removeAttribute(String attributeName) {
        jsActions.removeAttribute(getWrappedElement(), attributeName);
    }

    @Publish
    @Override public String getAttribute(String attributeName) { return jsActions.getAttribute(getWrappedElement(), attributeName); }

    @Publish
    public String getProperty(String propertyName) { return jsActions.getProperty(getWrappedElement(), propertyName); }

    public void setProperty(String property, Object value) {
        jsActions.setProperty(getWrappedElement(), property, value);
    }

    @Publish
    @Override public void click() {
        wrappedElement.click();
    }

    @Publish
    public void jsClick() {
        jsActions.click(getWrappedElement());
    }

    @Publish
    @Override public void submit() { wrappedElement.submit(); }

    @Publish
    @Override public void sendKeys(CharSequence... keysToSend) { wrappedElement.sendKeys(keysToSend); }

    @Publish
    @Override public void clear() {
        sendKeys(Keys.CONTROL + "a", Keys.DELETE);
        wrappedElement.clear();
    }

    @Publish
    @Override public String getTagName() { return wrappedElement.getTagName(); }

    @Publish
    @Override public boolean isSelected() { return wrappedElement.isSelected(); }

    @Publish
    @Override public boolean isEnabled() { return wrappedElement.isEnabled(); }

    @Publish
    @Override public String getText() { return wrappedElement.getText(); }

    @Publish
    public boolean isPresent() {
        try {
            wrappedElement.getTagName();
        } catch (WebDriverException ex) {
            return false;
        }

        return true;
    }

    @Publish
    @Override public boolean isDisplayed() {
        return isPresent() && wrappedElement.isDisplayed();
    }

    @Publish
    @Override public Point getLocation() {
        return wrappedElement.getLocation();
    }

    @Publish
    @Override public Dimension getSize() {
        return wrappedElement.getSize();
    }

    @Publish
    @Override public String getCssValue(String propertyName) {
        return wrappedElement.getCssValue(propertyName);
    }

    @Publish
    @Override public WebElement getWrappedElement() {
        return wrappedElement instanceof WrapsElement ? ((WrapsElement) wrappedElement).getWrappedElement() : wrappedElement;
    }

    @Publish
    public void waitUntil(int timeoutInSeconds, Predicate<WebElement> condition) {
        new FluentWait<WebElement>(this)
                .pollingEvery(1, TimeUnit.SECONDS)
                .withTimeout(timeoutInSeconds, TimeUnit.SECONDS)
                .withMessage(String.format("[%s] failed", condition))
                .ignoring(WebDriverException.class)
                .until(condition);
    }

    @Publish
    public void waitForHide(int seconds) {
        waitUntil(seconds, IS_HIDDEN);
    }

    @Publish
    public void waitForVisible(int seconds) {
        waitUntil(seconds, IS_VISIBLE);
    }

    @Publish
    public boolean isElementDisplayed(By by) {
        for (WebElement element : findElements(by)) {
            if (element.isDisplayed()) { return true; }
        }
        return false;
    }

    @Publish
    public boolean isTextPresent(String text) {
        return getHtml().contains(text);
    }

    @Publish
    public boolean isTextDisplayed(String text) {
        return isTextPresent(text) && isElementDisplayed(By
                .xpath(String.format(".//*[%s]", containstext("text()", text))));
    }

    @Publish
    public boolean isValueDisplayed(String value) {
        return isTextPresent(value) && isElementDisplayed(By
                .xpath(String.format(".//*[%s]", containstext("@value", value))));
    }

    @Override public String toString() {
        return jsActions.toString(getWrappedElement());
    }

}