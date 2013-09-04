package com.astound.fragments.proxy;

import com.astound.fragments.FragmentFactory;
import com.astound.fragments.elements.Fragment;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;

import static com.astound.fragments.utils.StackTraceCleaner.cleanStackTrace;

public class ListLoader<F extends Fragment> implements InvocationHandler {

    private final FragmentFactory fragmentFactory;

    private final ElementLocator locator;

    private final Class<F> aClass;

    private final String name;

    private List<Fragment> cachedList;

    public ListLoader(Class<F> aClass, ElementLocator locator, FragmentFactory fragmentFactory, String name) {
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
            cachedList.add(fragmentFactory.createFragment(aClass, itemLocator(i), itemName(i)));
        }

        return cachedList;
    }

    private ElementLocator itemLocator(int index) {
        return new ListItemLocator(index, locator);
    }

    private String itemName(int index) {
        return String.format("%s-element-%s", name, index);
    }
}
