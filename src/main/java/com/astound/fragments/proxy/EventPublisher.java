package com.astound.fragments.proxy;

import com.astound.fragments.PageContext;
import com.astound.fragments.events.EventBusService;
import com.astound.fragments.events.PageEvent;
import com.astound.fragments.events.PublishEvent;
import com.google.common.eventbus.EventBus;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static com.astound.fragments.MessageFormatter.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class EventPublisher implements MethodInterceptor {

    private final PageContext pageContext;

    public EventPublisher(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    @Override public Object intercept(Object o, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
        PublishEvent publishEvent = method.getAnnotation(PublishEvent.class);

        if (publishEvent != null) {
            getEventBus().post(newEvent(publishEvent, method, arguments));
        }

        return methodProxy.invokeSuper(pageContext, arguments);
    }

    private PageEvent newEvent(PublishEvent publishEvent, Method method, Object[] arguments) {
        String eventDescription = isEmpty(publishEvent.value()) ? method.getName() : format(publishEvent.value(), arguments);

        return new PageEvent(pageContext.getName(), eventDescription, publishEvent.type());
    }

    private static EventBus getEventBus() {
        return EventBusService.getInstance().get();
    }
}
