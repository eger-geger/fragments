package com.astound.fragments.proxy;

import com.astound.fragments.PageWithFragments;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;

import static com.astound.fragments.utils.StackTraceUtils.cleanStackTrace;

public class FrameLoader extends ElementLoader {

    private final static List<String> SEARCH_CONTEXT_METHODS = Arrays.asList("findElement", "findElements");

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
        if (SEARCH_CONTEXT_METHODS.contains(method.getName())) {
            try {
                return invokeSearchContext(method, args);
            } catch (InvocationTargetException | UndeclaredThrowableException ex) {
                throw cleanStackTrace(ex.getCause());
            }
        }
        return super.invoke(proxy, method, args);
    }

    private Object invokeSearchContext(Method method, Object[] args) throws Throwable {
        pageObject.switchToFrame(frameWebElement);

        Object retvalue = method.invoke(pageObject, args);

        pageObject.switchToPage();

        return retvalue;
    }

}
