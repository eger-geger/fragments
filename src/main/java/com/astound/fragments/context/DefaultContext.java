package com.astound.fragments.context;

import com.astound.fragments.FragmentFactory;
import com.astound.fragments.elements.Fragment;
import com.astound.fragments.proxy.ElementLazyLoader;
import org.openqa.selenium.*;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.util.Collections;
import java.util.List;

import static java.lang.reflect.Proxy.newProxyInstance;

public class DefaultContext implements FragmentContext {

    private class DelegatingElementLocator implements ElementLocator {

        private final By by;

        private DelegatingElementLocator(By by) {
            this.by = by;
        }

        @Override public WebElement findElement() {
            return searchContext.findElement(by);
        }

        @Override public List<WebElement> findElements() {
            return searchContext.findElements(by);
        }
    }

    private final String contextName;

    private final SearchContext searchContext;

    private final JavascriptExecutor jsExecutor;

    private final FragmentFactory fragmentFactory;

    public DefaultContext(SearchContext searchContext, JavascriptExecutor jsExecutor, FragmentFactory fragmentFactory, String contextName) {
        this.contextName = contextName;
        this.fragmentFactory = fragmentFactory;
        this.searchContext = searchContext;
        this.jsExecutor = jsExecutor;
    }

    @Override public String getName() {
        return contextName;
    }

    @Override public WebElement getRootElement() {
        return lazyLoadedRoot(searchContext);
    }

    @Override public Fragment findFragment(By by) {
        return findFragment(by, Fragment.class);
    }

    @Override public List<Fragment> findFragments(By by) {
        return findFragments(by, Fragment.class);
    }

    @Override public <E extends Fragment> E findFragment(By by, Class<E> aClass) {
        return findFragment(by, aClass, buildAnonymousName(by, aClass));
    }

    @Override public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass) {
        return findFragments(by, aClass, buildAnonymousName(by, aClass));
    }

    @Override public <E extends Fragment> E findFragment(By by, Class<E> aClass, String name) {
        return fragmentFactory.createFragment(aClass, new DelegatingElementLocator(by), name);
    }

    @Override public <E extends Fragment> List<E> findFragments(By by, Class<E> aClass, String name) {
        return fragmentFactory.createList(aClass, new DelegatingElementLocator(by), name);
    }

    @Override public List<WebElement> findElements(By by) {return searchContext.findElements(by);}

    @Override public WebElement findElement(By by) {return searchContext.findElement(by);}

    private String buildAnonymousName(By by, Class aClass) {
        return String.format("[%s] found in [%s] with locator [%s]", aClass.getSimpleName(), this.getName(), by);
    }

    @Override public Object executeScript(String s, Object... objects) {return jsExecutor.executeScript(s, objects);}

    @Override public Object executeAsyncScript(String s, Object... objects) {return jsExecutor.executeAsyncScript(s, objects);}

    private static WebElement lazyLoadedRoot(final SearchContext searchContext) {
        ClassLoader classLoader = searchContext.getClass().getClassLoader();

        InvocationHandler invocationHandler = new ElementLazyLoader(new ElementLocator() {
            @Override public WebElement findElement() {
                if (searchContext instanceof WebElement) {
                    return (WebElement) searchContext;
                } else if (searchContext instanceof WebDriver) {
                    return searchContext.findElement(By.tagName("body"));
                } else {
                    return searchContext.findElement(By.xpath("."));
                }
            }

            @Override public List<WebElement> findElements() { return Collections.emptyList(); }
        });

        return (WebElement) newProxyInstance(classLoader, new Class[]{WebElement.class, WrapsElement.class}, invocationHandler);
    }
}
