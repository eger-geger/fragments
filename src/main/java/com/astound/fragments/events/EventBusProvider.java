package com.astound.fragments.events;

import com.google.common.eventbus.EventBus;

public interface EventBusProvider {

    public EventBus get();

}
