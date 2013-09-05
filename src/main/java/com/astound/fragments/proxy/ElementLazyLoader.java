package com.astound.fragments.proxy;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import static com.astound.fragments.utils.StackTraceCleaner.cleanStackTrace;

public class ElementLazyLoader implements InvocationHandler {

    private final static String M_GET_WRAPPED = "getWrappedElement";

    private final ElementCache elementCache;

    public ElementLazyLoader(ElementLocator locator) {
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

    private static class ElementCache {

        private WebElement cached;

        private final ElementLocator locator;

        ElementCache(ElementLocator locator) {
            this.locator = locator;
        }

        public WebElement getElement() {
            if (cached == null) {
                cached = locator.findElement();
            }

            invalidateCache();

            return cached;
        }

        private void invalidateCache() {
            try {
                cached.getTagName();
            } catch (StaleElementReferenceException ex) {
                cached = locator.findElement();
            }
        }

    }
}
