package com.astound.fragments;

import com.astound.fragments.context.DefaultContext;
import com.astound.fragments.context.FragmentContext;
import com.astound.fragments.context.FrameContext;
import com.astound.fragments.elements.Fragment;
import com.astound.fragments.events.PublisherFactory;
import com.astound.fragments.locators.FragmentLocatorFactory;
import com.astound.fragments.proxy.ElementLazyLoader;
import com.astound.fragments.proxy.ListLazyLoader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Field;
import java.util.List;

import static com.astound.fragments.utils.ReflectionUtils.assignField;
import static java.lang.reflect.Proxy.newProxyInstance;

public class FragmentFactory {

    private static final Class[] FRAGMENT_LIST_INTERFACES = new Class[]{List.class};

    private static final Class[] FRAGMENT_INTERFACES = new Class[]{WebElement.class, WrapsElement.class};

    private final ClassLoader classLoader;

    private final WebDriver webDriver;

    private final PublisherFactory publisherFactory;

    public FragmentFactory(WebDriver webDriver) {
        classLoader = getClass().getClassLoader();
        publisherFactory = new PublisherFactory();
        this.webDriver = webDriver;
    }

    public <F extends Fragment> F createFragment(Class<F> aClass, ElementLocator locator, String name) {
        F fragment = newFragment(aClass, createDefaultContext(createWebElementProxy(locator), name));

        initFragmentsIn(fragment);

        return fragment;
    }

    public <F extends Fragment> F createFrame(Class<F> fClass, ElementLocator locator, String name) {
        F fragment = newFragment(fClass, createFrameContext(createFragment(Fragment.class, locator, name), name));

        initFragmentsIn(fragment);

        return fragment;
    }

    public FragmentContext createFrameContext(WebElement frameWebElement, String name) {
        return new FrameContext(frameWebElement, webDriver, this, name);
    }

    public FragmentContext createDefaultContext(SearchContext searchContext, String name) {
        return new DefaultContext(searchContext, (JavascriptExecutor) webDriver, this, name);
    }

    private <F extends Fragment> F newFragment(Class<F> fClass, FragmentContext fragmentContext) {
        return publisherFactory.createPublishingInstance(fClass, new Class[]{FragmentContext.class}, fragmentContext);
    }

    public <F extends Fragment> List<F> createList(Class<F> aClass, ElementLocator locator, String name) {
        return (List<F>) newProxyInstance(classLoader, FRAGMENT_LIST_INTERFACES, new ListLazyLoader<>(aClass, locator, this, name));
    }

    private WebElement createWebElementProxy(ElementLocator locator) {
        return (WebElement) newProxyInstance(classLoader, FRAGMENT_INTERFACES, new ElementLazyLoader(locator));
    }

    public <Context extends FragmentContext> void initFragmentsIn(Context context) {
        initFragmentsIn(context, new FragmentDecorator(new FragmentLocatorFactory(context), this));
    }

    public <Context extends FragmentContext> void initFragmentsIn(Context context, FragmentDecorator decorator) {
        Class aClass = context.getClass();

        while (isAssignableContext(aClass)) {
            for (Field field : aClass.getDeclaredFields()) {
                Object value = decorator.decorate(classLoader, field);

                if (value != null && isAssignableClass(field.getType())) {
                    assignField(context, field, value);
                }
            }
            aClass = aClass.getSuperclass();
        }
    }

    private static boolean isAssignableClass(Class aClass) {
        return WebElement.class.isAssignableFrom(aClass) || List.class.isAssignableFrom(aClass);
    }

    private static boolean isAssignableContext(Class aClass) {
        return FragmentContext.class.isAssignableFrom(aClass) && !aClass.equals(Fragment.class);
    }

}