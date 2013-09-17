# Fragments

Define complex page object as a set of smaller components.

## Define Page with fragments

```java
public class WikiHomePage extends WikiPage {

	public WikiHomePage(WebDriver webDriver) {
		super(webDriver);
	}

	@FindBys({@FindBy(id = "p-navigation"), @FindBy(tagName = "a")})
	private List<Fragment> navigationLinks;

	@FindBy(id = "p-participation")
	public CollapsibleMenu participationMenu;
}
```

## Init Fragments

```java
package com.astound.fragments;

import com.astound.fragments.context.FragmentContext;
import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class PageWithFragments implements FragmentContext {

    protected final WebDriver webDriver;

    private final FragmentContext context;

    public PageWithFragments(final WebDriver webDriver) {
        final String contextName = getClass().getSuperclass().getName();

        FragmentFactory fragmentFactory = new FragmentFactory(webDriver);
        context = fragmentFactory.createDefaultContext(webDriver, contextName);
        fragmentFactory.initFragmentsIn(this);

        this.webDriver = webDriver;
    }

    @Override
    public String getName() {
        return context.getName();
    }

    @Override
    public WebElement getRootElement() {
        return context.getRootElement();
    }

    @Override
    public Fragment findFragment(By by) {
        return context.findFragment(by);
    }

    @Override
    public List<Fragment> findFragments(By by) {
        return context.findFragments(by);
    }

    @Override
    public <E extends Fragment> E findFragment(By by, Class<E> aClass) {
        return context.findFragment(by, aClass);
    }

    @Override
    public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass) {
        return context.findFragments(by, aClass);
    }

    @Override
    public <E extends Fragment> E findFragment(By by, Class<E> aClass, String name) {
        return context.findFragment(by, aClass, name);
    }

    @Override
    public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass, String name) {
        return context.findFragments(by, aClass, name);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return context.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return context.findElement(by);
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        return context.executeScript(s, objects);
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        return context.executeAsyncScript(s, objects);
    }

    protected abstract class LocalFrameHandler<T extends Fragment> extends FrameHandler<T> {
        public LocalFrameHandler() { super(webDriver); }
    }
}
```

Supported element annotations:

1. Native: `@FindBy`, `@FindByAll`, `@FindBys`
2. Custom: `@Frame`

Page which is going to support fragments initialization should implement `FragmentContext`.

There are 4 options:

1. Implement interface by hand
2. Delegate to new instance of `DefaultFragmentContext`
3. Use `FragmentFactory` to create `DefaultFragmentContext` for you and delegate to it
4. Extend `PageWithFragments`

## Custom Fragments

```java
import com.astound.fragments.context.FragmentContext;
import com.astound.fragments.elements.Fragment;
import com.google.common.base.Predicate;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CollapsibleMenu extends Fragment {

    public CollapsibleMenu(FragmentContext fragmentContext) {
        super(fragmentContext);
    }

    @FindBy(tagName = "a")
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
                @Override public boolean apply(WebElement input) {
                    return isOpened();
                }
            });
        }
    }

    public void hide() {
        if (isOpened()) {
            headerToggle.click();

            waitUntil(5, new Predicate<WebElement>() {
                @Override public boolean apply(WebElement input) {
                    return isClosed();
                }
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
```

Custom fragments should extend other fragment and provide visible constructor which takes `FragmentContext` as argument.
Fragment may contain any other web elements or fragments definitions.

## Frame handling

Mark element with `@Frame` annotation and use subclass of `FrameHandler` to define action

```java

import com.astound.fragments.Frame;
import com.astound.fragments.FrameHandler;
import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class PageWithFrame extends BasePage {

    @FindBy(id = "frameId")
    @Frame private Fragment frameFragment;

    public PageWithFrame(WebDriver webDriver) {
        super(webDriver);
    }

    public void doWithLocalFrameHandler(){
        new LocalFrameHandler<Fragment>(){
            @Override protected void doPerform(Fragment frame) {
                //Perform some actions within frame here
            }
        }.perform(frameFragment);   // And provide frame
    }

    public void doWithFrameHandler(){
        new FrameHandler<Fragment>(webDriver){
            @Override protected void doPerform(Fragment frame) {
                //Perform some actions within frame here.
            }
        }.perform(frameFragment);   //And provide frame
    }

}

```

## Custom Element Locating

Implement interface `ByExtractor` and register implementation as service.

```java

public class FromCustomFieldProperty implements ByExtractor {

    @Override public Optional<By> extract(Field field) {
        return Optional.<By>of(/* your custom logic*/).or(Optional.empty()); //implement custom extractor or return empty optional if not applicable
    }
}

```

Now you can generate whatever locator you want, based on any field property you like. (after registering your implementation as service)

## Event Publishing

Any fragment action marked with `@Publish` will publish event object into provided event bus.

First register `EventBusProvider` implementation as service. It should provide guava `EventBus` used for channelling the events.
Next register your event handlers with `EventBus`. It is very convenient to register event handlers inside your custom provider.
Nevertheless provided `EventBus` can also be acquired from `EventBusService`.

```java

public class EventBusProvider implements com.astound.fragments.events.EventBusProvider {

    private final EventBus eventBus = new EventBus();

    public EventBusProvider() {
        eventBus.register(new FragmentLogger());
    }

    @Override public EventBus get() {
        return eventBus;
    }
}

```

Events are represented by `Event` class. Here is simple event logger (using slf4j):

```java

public class FragmentLogger {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Subscribe @AllowConcurrentEvents
	public void handle(Event event) {
		logger.info("{} - {} :: {} ", event.getEventType(), event.getContextName(), event.getEventDescription());
	}

}

```

Fragments out of the box are publishing a lot of events. Just add `@Publish` annotation to any custom fragment method to make it publishing the events too.
Annotation supports 2 properties : `type` (`WEB_DRIVER_EVENT`, `PAGE_EVENT`, `USER_EVENT`) and `format` (`Event#description`).

Here is formatting options:

* {m} or {method} - replaced with method name
* {r} or {ret} or {return} - replaced with method return value
* {1-9} - replaced with method argument in appropriate position

Anything else left unchanged.

Actually you can make any object publish the events. Just create it with `PublisherFactory`:

```java

...

    PublisherFactory publisherFactory = new PublisherFactory();

    SomePage somePublishingPage = publisherFactory.createPublishingInstance(SomePage.class, new Class[] {WebDriver.class}, webDriver);

...

```

