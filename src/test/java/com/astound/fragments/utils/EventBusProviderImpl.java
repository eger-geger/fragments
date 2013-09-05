package com.astound.fragments.utils;

import com.astound.fragments.events.EventBusProvider;
import com.google.common.eventbus.EventBus;

public class EventBusProviderImpl implements EventBusProvider {

    private final EventBus eventBus;

    public EventBusProviderImpl() {
        this.eventBus = new EventBus();
    }

    @Override public EventBus get() {
        return eventBus;
    }
}
