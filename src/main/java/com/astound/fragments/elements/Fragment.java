package com.astound.fragments.elements;

import com.astound.fragments.FragmentContext;
import com.astound.fragments.events.EventType;
import com.astound.fragments.events.Publish;
import com.astound.fragments.proxy.ElementLazyLoader;
import com.astound.fragments.utils.JSActions;
import com.google.common.base.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.InvocationHandler;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.astound.fragments.utils.XPathFunctions.containstext;
import static java.lang.reflect.Proxy.newProxyInstance;

public class Fragment implements WebElement, WrapsElement, FragmentContext {

	private static final Predicate<WebElement> IS_VISIBLE = new Predicate<WebElement>() {
		@Override public boolean apply(WebElement input) { return input.isDisplayed(); }
	};

	private static final Predicate<WebElement> IS_HIDDEN = new Predicate<WebElement>() {
		@Override public boolean apply(WebElement input) { return !input.isDisplayed(); }
	};

	private final FragmentContext wrappedContext;

	private final WebElement wrappedElement;

	protected final JSActions jsActions;

	public Fragment(FragmentContext fragmentContext) {
		wrappedContext = fragmentContext;
		wrappedElement = lazyLoadedRoot(fragmentContext);
		jsActions = new JSActions(this);
	}

	private static WebElement lazyLoadedRoot(final SearchContext searchContext) {
		ClassLoader classLoader = searchContext.getClass().getClassLoader();

		InvocationHandler invocationHandler = new ElementLazyLoader(new ElementLocator() {
			@Override public WebElement findElement() {
				return searchContext instanceof WebElement
						? (WebElement) searchContext
						: searchContext.findElement(By.xpath("."));
			}

			@Override public List<WebElement> findElements() { return null; }
		});

		return (WebElement) newProxyInstance(classLoader, new Class[] {WebElement.class, WrapsElement.class}, invocationHandler);
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

	@Publish(format = "element HTML is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	public String getHtml() { return jsActions.getHtml(getWrappedElement()); }

	@Publish(format = "element text content is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	public String getTextContent() { return jsActions.getTextContent(getWrappedElement()); }

	@Publish(format = "element normalized content is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	public String getNormalizedTextContent() { return StringUtils.normalizeSpace(getTextContent()); }

	@Publish(format = "setting element attribute [{1}] to [{2}]", type = EventType.WEB_DRIVER_EVENT)
	public void setAttribute(String attributeName, String attributeValue) {
		jsActions.setAttribute(getWrappedElement(), attributeName, attributeValue);
	}

	@Publish(format = "removing element attribute [{1}]", type = EventType.WEB_DRIVER_EVENT)
	public void removeAttribute(String attributeName) {
		jsActions.removeAttribute(getWrappedElement(), attributeName);
	}

	@Publish(format = "element attribute [{1}] value is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public String getAttribute(String attributeName) { return jsActions.getAttribute(getWrappedElement(), attributeName); }

	@Publish(format = "element property [{1}] value is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	public String getProperty(String propertyName) { return jsActions.getProperty(getWrappedElement(), propertyName); }

	@Publish(format = "setting element property [{1}] to [{2}]", type = EventType.WEB_DRIVER_EVENT)
	public void setProperty(String property, Object value) {
		jsActions.setProperty(getWrappedElement(), property, value);
	}

	@Publish(format = "performing click on element", type = EventType.WEB_DRIVER_EVENT)
	@Override public void click() {
		wrappedElement.click();
	}

	@Publish(format = "emulating click on element with javascript", type = EventType.WEB_DRIVER_EVENT)
	public void jsClick() {
		jsActions.click(getWrappedElement());
	}

	@Publish(format = "submitting element", type = EventType.WEB_DRIVER_EVENT)
	@Override public void submit() { wrappedElement.submit(); }

	@Publish(format = "entering [{1}] into element", type = EventType.WEB_DRIVER_EVENT)
	@Override public void sendKeys(CharSequence... keysToSend) { wrappedElement.sendKeys(keysToSend); }

	@Publish(format = "removing element text content", type = EventType.WEB_DRIVER_EVENT)
	@Override public void clear() {
		sendKeys(Keys.CONTROL + "a", Keys.DELETE);
		wrappedElement.clear();
	}

	@Publish(format = "element is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public String getTagName() { return wrappedElement.getTagName(); }

	@Publish(format = "element selected is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public boolean isSelected() { return wrappedElement.isSelected(); }

	@Publish(format = "element enabled is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public boolean isEnabled() { return wrappedElement.isEnabled(); }

	@Publish(format = "element text is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public String getText() { return wrappedElement.getText(); }

	@Publish(format = "element present is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	public boolean isPresent() {
		try {
			wrappedElement.getTagName();
		} catch (WebDriverException ex) {
			return false;
		}

		return true;
	}

	@Publish(format = "element visible is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public boolean isDisplayed() {
		return isPresent() && wrappedElement.isDisplayed();
	}

	@Publish(format = "element location is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public Point getLocation() {
		return wrappedElement.getLocation();
	}

	@Publish(format = "element size is [{return}]", type = EventType.WEB_DRIVER_EVENT)
	@Override public Dimension getSize() {
		return wrappedElement.getSize();
	}

	@Publish(format = "element css property [{1}] is [{return}]")
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

	@Publish(format = "waiting until element is hidden for [{1}] seconds", type = EventType.PAGE_EVENT)
	public void waitForHide(int seconds) {
		waitUntil(seconds, IS_HIDDEN);
	}

	@Publish(format = "waiting until element is visible for [{1}] seconds", type = EventType.PAGE_EVENT)
	public void waitForVisible(int seconds) {
		waitUntil(seconds, IS_VISIBLE);
	}

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