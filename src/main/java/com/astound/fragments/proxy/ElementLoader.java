package com.astound.fragments.proxy;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import static com.astound.fragments.utils.StackTraceCleaner.cleanStackTrace;

public class ElementLoader implements InvocationHandler {

    private final static String M_GET_WRAPPED = "getWrappedElement";

    private final ElementCache elementCache;

    public ElementLoader(ElementLocator locator) {
        elementCache = new ElementCache(locator);
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            WebElement target = elementCache.getElement();

            return method.getName().equals(M_GET_WRAPPED) ? target : method.invoke(target, args);
        } catch (InvocationTargetException | UndeclaredThrowableException ex) {
            throw cleanStackTrace(ex.getCause());
        }
    }

}
