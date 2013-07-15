package com.astound.fragments.proxy;

import com.astound.fragments.FragmentFactory;
import com.astound.fragments.FragmentSettings;
import com.astound.fragments.elements.Fragment;
import com.google.common.base.Function;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.astound.fragments.utils.StackTraceUtils.cleanStackTrace;

public class ListLoader<F extends Fragment<?>> implements InvocationHandler {

    private class ListItemLocator implements ElementLocator {

        private final int index;

        public ListItemLocator(int index) { this.index = index; }

        public WebElement findElement() {
            try {
                return locator.findElements().get(index);
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException(String.format("List item [%s] not found!", index));
            }
        }

        public List<WebElement> findElements() {
            throw new UnsupportedOperationException("Finding for elements inside list item locators");
        }

        public String toString() { return locator.toString(); }

    }

    private static final Function<ElementLocator, List<WebElement>> FIND_ELEMENTS = new Function<ElementLocator, List<WebElement>>() {
        public List<WebElement> apply(ElementLocator locator) { return locator.findElements(); }
    };

    private final FragmentFactory fragmentFactory;

    private final ElementLocator locator;

    private final Class<F> type;

    private final String name;

    public ListLoader(Class<F> type, ElementLocator locator, FragmentFactory fragmentFactory, String name) {
        this.fragmentFactory = fragmentFactory;
        this.locator = locator;
        this.type = type;
        this.name = name;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elementList = loadElements();

        List<Fragment<?>> target = new ArrayList<>();

        for (int i = 0; i < elementList.size(); i++) {
            target.add(fragmentFactory.createFragment(type,
                    new ListItemLocator(i), String.format("%s-element-%s", name, i)));
        }

        try {
            return method.invoke(target, args);
        } catch (InvocationTargetException | UndeclaredThrowableException ex) {
            throw cleanStackTrace(ex.getCause());
        }
    }

    private List<WebElement> loadElements() {
        return new FluentWait<>(locator)
                .pollingEvery(500, TimeUnit.MILLISECONDS)
                .withTimeout(FragmentSettings.getInstance().getImplicitWait(), TimeUnit.MILLISECONDS)
                .ignoring(WebDriverException.class)
                .until(FIND_ELEMENTS);
    }
}
