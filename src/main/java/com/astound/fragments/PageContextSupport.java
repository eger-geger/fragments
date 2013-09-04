package com.astound.fragments;

import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.util.List;

public class PageContextSupport implements PageContext {

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

    public PageContextSupport(SearchContext searchContext, JavascriptExecutor jsExecutor, FragmentFactory fragmentFactory, String contextName) {
        this.contextName = contextName;
        this.fragmentFactory = fragmentFactory;
        this.searchContext = searchContext;
        this.jsExecutor = jsExecutor;
    }

    @Override public String getName() {
        return contextName;
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
}
