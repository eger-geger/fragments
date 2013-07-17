package com.astound.fragments.proxy;

import com.astound.fragments.PageWithFragments;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;

import static com.astound.fragments.utils.StackTraceUtils.cleanStackTrace;

public class FrameLoader extends ElementLoader {

    private static final Method SC_FIND_ELEMENT, SC_FIND_ELEMENTS;

    private static final Method WB_FIND_ELEMENT, WB_FIND_ELEMENTS;

    static {
        try {
            SC_FIND_ELEMENT = SearchContext.class.getDeclaredMethod("findElement", By.class);
            SC_FIND_ELEMENTS = SearchContext.class.getDeclaredMethod("findElements", By.class);
            WB_FIND_ELEMENT = WebElement.class.getDeclaredMethod("findElement", By.class);
            WB_FIND_ELEMENTS = WebElement.class.getDeclaredMethod("findElements", By.class);
        } catch (NoSuchMethodException ex) {
            throw new IllegalArgumentException("Method signature have changed", ex);
        }
    }

    private final PageWithFragments pageObject;

    private final WebElement frameWebElement;

    public FrameLoader(PageWithFragments page, final WebElement frame) {
        super(new ElementLocator() {

            public List<WebElement> findElements() {
                throw new UnsupportedOperationException("Cannot locate multiple frames");
            }

            public WebElement findElement() {
                return frame;
            }
        });

        this.pageObject = page;
        this.frameWebElement = frame;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (WB_FIND_ELEMENT.equals(method) || SC_FIND_ELEMENT.equals(method)) {
                return SC_FIND_ELEMENT.invoke(pageObject, args);
            } else if (WB_FIND_ELEMENTS.equals(method) || SC_FIND_ELEMENTS.equals(method)) {
                return SC_FIND_ELEMENTS.invoke(pageObject, args);
            }
        } catch (InvocationTargetException | UndeclaredThrowableException ex) {
            throw cleanStackTrace(ex.getCause());
        }

        return super.invoke(proxy, method, args);
    }
}
