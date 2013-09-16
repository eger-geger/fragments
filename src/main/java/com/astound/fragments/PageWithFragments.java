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
