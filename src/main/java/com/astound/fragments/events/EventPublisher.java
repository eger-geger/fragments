package com.astound.fragments.events;

import com.astound.fragments.context.FragmentContext;
import com.astound.fragments.format.DefaultToStringConverter;
import com.astound.fragments.format.Formatter;
import com.astound.fragments.format.transformers.ArrayTransformer;
import com.astound.fragments.format.transformers.ObjectTransformer;
import com.astound.fragments.format.transformers.StringTransformer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import static com.astound.fragments.utils.ReflectionUtils.isOverridden;

class EventPublisher implements MethodInterceptor {

    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("\\{(m|method)\\}");

    private static final Pattern RETURN_VALUE_PATTERN = Pattern.compile("\\{(r|ret|return)\\}");

    private static final Method CONTEXT_GET_NAME;

    static {
        try {
            CONTEXT_GET_NAME = FragmentContext.class.getMethod("getName");
        } catch (NoSuchMethodException ex) {
            throw new RuntimeException("Something has gone CraZy!");
        }
    }

    private final Formatter formatter = new Formatter(new DefaultToStringConverter());

    @Override public Object intercept(Object object, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
        final Object returnValue = methodProxy.invokeSuper(object, arguments);

        Publish publish = method.getAnnotation(Publish.class);

        if (shouldPublish(publish, method)) {
            String description = formatter.format(publish.format(), argumentTransformer(arguments),
                    returnValueTransformer(returnValue), methodTransformer(method));

            postToEventBus(new Event(nameOf(object), description, publish.type()));
        }

        return returnValue;
    }

    private static StringTransformer argumentTransformer(Object[] arguments) {
        return new ArrayTransformer(arguments);
    }

    private static StringTransformer methodTransformer(Method method) {
        return new ObjectTransformer(METHOD_NAME_PATTERN, isVoid(method) ? "void" : method.getName());
    }

    private static StringTransformer returnValueTransformer(Object object) {
        return new ObjectTransformer(RETURN_VALUE_PATTERN, object);
    }

    private static String nameOf(Object object) {
        if (object instanceof FragmentContext) {
            return ((FragmentContext) object).getName();
        } else {
            return object.getClass().getSuperclass().getSimpleName();
        }
    }

    private static boolean isVoid(Method method) {
        return method.getReturnType().equals(Void.TYPE);
    }

    private static boolean shouldPublish(Publish publish, Method method) {
        return publish != null && !isOverridden(CONTEXT_GET_NAME, method);
    }

    private static void postToEventBus(Object object) {
        EventBusService.getInstance().get().post(object);
    }
}
