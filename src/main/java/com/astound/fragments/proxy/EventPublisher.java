package com.astound.fragments.proxy;

import com.astound.fragments.PageContext;
import com.astound.fragments.events.EventBusService;
import com.astound.fragments.events.PageEvent;
import com.astound.fragments.events.Publish;
import com.astound.fragments.events.PublishFormatter;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class EventPublisher implements MethodInterceptor {

    private static final List<String> IGNORE_LIST = Arrays.asList(new String[]{"getName"});

    @Override public Object intercept(Object object, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
        final Object returnValue = methodProxy.invokeSuper(object, arguments);

        Publish publish = method.getAnnotation(Publish.class);

        if (shouldPublish(publish, method)) {
            PublishFormatter formatter = new PublishFormatter(publish);
            formatter.formatMethod(method);
            formatter.formatArguments(arguments);
            formatter.formatContextName(contextNameOf(object));
            formatter.formatReturnValue(isVoid(method) ? "VOID" : returnValue);

            postToEventBus(new PageEvent(contextNameOf(object), formatter.getFormatted(), publish.type()));
        }

        return returnValue;
    }

    private static String contextNameOf(Object object) {
        if (object instanceof PageContext) {
            return ((PageContext) object).getName();
        } else {
            return object.getClass().getSimpleName();
        }
    }

    private static boolean isVoid(Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    private static boolean shouldPublish(Publish publish, Method method) {
        return publish != null && !IGNORE_LIST.contains(method.getName());
    }

    private static void postToEventBus(Object object) {
        EventBusService.getInstance().get().post(object);
    }
}
