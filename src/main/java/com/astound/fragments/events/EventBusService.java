package com.astound.fragments.events;

import com.google.common.eventbus.EventBus;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;

public class EventBusService implements EventBusProvider {

    private static EventBusService instance;

    private final EventBus proxyBus;

    private EventBusService() {
        final Iterable<EventBusProvider> providers = ServiceLoader.load(EventBusProvider.class);

        proxyBus = (EventBus) Enhancer.create(EventBus.class, new MethodInterceptor() {
            @Override public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Iterator<EventBusProvider> providerIterator = providers.iterator();

                while (providerIterator.hasNext()) {
                    method.invoke(providerIterator.next().get(), objects);
                }

                return null;
            }
        });

    }

    @Override public EventBus get() { return proxyBus; }

    public static EventBusProvider getInstance() {
        if (instance == null) {
            instance = new EventBusService();
        }
        return instance;
    }
}