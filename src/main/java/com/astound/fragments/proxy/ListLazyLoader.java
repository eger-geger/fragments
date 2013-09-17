package com.astound.fragments.proxy;

import com.astound.fragments.FragmentFactory;
import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import static com.astound.fragments.utils.StackTraceCleaner.cleanStackTrace;

/** Performs list elements locating and cashing of located elements. Cash is updated if newly located list size differs from cashed list size */
public class ListLazyLoader<F extends Fragment> implements InvocationHandler {

    private final FragmentFactory fragmentFactory;

    private final ElementLocator locator;

    private final Class<F> aClass;

    private final String name;

    private List<Fragment> cachedList;

    public ListLazyLoader(Class<F> aClass, ElementLocator locator, FragmentFactory fragmentFactory, String name) {
        this.fragmentFactory = fragmentFactory;
        this.locator = locator;
        this.aClass = aClass;
        this.name = name;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<WebElement> elementList = locator.findElements();

        if (cachedList == null || cachedList.size() != elementList.size()) {
            cachedList = toCachedList(elementList);
        }

        try {
            return method.invoke(cachedList, args);
        } catch (InvocationTargetException | UndeclaredThrowableException ex) {
            throw cleanStackTrace(ex.getCause());
        }
    }

    private List<Fragment> toCachedList(List<WebElement> elementList) {
        List<Fragment> cachedList = new ArrayList<>();

        for (int i = 0; i < elementList.size(); i++) {
            cachedList.add(fragmentFactory.createFragment(aClass, new ListItemLocator(i), itemName(i)));
        }

        return cachedList;
    }

    private String itemName(int index) {
        return String.format("%s-element-%s", name, index);
    }

    private class ListItemLocator implements ElementLocator {

        private final int index;

        ListItemLocator(int index) {
            this.index = index;
        }

        @Override public WebElement findElement() {
            try {
                return locator.findElements().get(index);
            } catch (IndexOutOfBoundsException ex) {
                throw new NoSuchElementException(String.format("List item [%s] not found!", index));
            }
        }

        @Override public List<WebElement> findElements() {
            throw new UnsupportedOperationException("Should not be used for ListItemLocator");
        }

        @Override public String toString() {
            return locator.toString();
        }

    }
}
