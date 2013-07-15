package com.astound.fragments.proxy;

import com.astound.fragments.FragmentSettings;
import com.google.common.base.Function;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.FluentWait;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.TimeUnit;

import static com.astound.fragments.utils.StackTraceUtils.cleanStackTrace;

public class ElementLoader implements InvocationHandler {

    private final static String M_GET_WRAPPED = "getWrappedElement";

    private static final Function<ElementLocator, WebElement> FIND_FRAGMENT = new Function<ElementLocator, WebElement>() {
        public WebElement apply(ElementLocator locator) { return locator.findElement(); }
    };

    private final ElementLocator locator;

    public ElementLoader(ElementLocator locator) { this.locator = locator; }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            WebElement target = loadElement();

            return method.getName().equals(M_GET_WRAPPED) ? target : method.invoke(target, args);
        } catch (InvocationTargetException | UndeclaredThrowableException ex) {
            throw cleanStackTrace(ex.getCause());
        } catch (TimeoutException ex) {
            throw cleanStackTrace(new NoSuchElementException(String.format("Failed to locate element [%s]", locator)));
        }
    }

    private WebElement loadElement() {
        return new FluentWait<>(locator)
                .withTimeout(FragmentSettings.getInstance().getImplicitWait(), TimeUnit.MILLISECONDS)
                .pollingEvery(500, TimeUnit.MILLISECONDS).ignoring(WebDriverException.class)
                .until(FIND_FRAGMENT);
    }
}
