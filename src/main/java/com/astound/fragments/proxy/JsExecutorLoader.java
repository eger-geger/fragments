package com.astound.fragments.proxy;

import com.astound.fragments.JsExecutorProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.astound.fragments.utils.StackTraceCleaner.cleanStackTrace;

public class JsExecutorLoader implements InvocationHandler {

    private final JsExecutorProvider jsExecutorProvider;

    public JsExecutorLoader(JsExecutorProvider jsExecutorProvider) {
        this.jsExecutorProvider = jsExecutorProvider;
    }

    @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(jsExecutorProvider.get(), args);
        } catch (InvocationTargetException ex) {
            throw cleanStackTrace(ex);
        }
    }
}
