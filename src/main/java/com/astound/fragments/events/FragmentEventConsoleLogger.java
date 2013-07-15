package com.astound.fragments.events;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

public class FragmentEventConsoleLogger {

    @Subscribe @AllowConcurrentEvents
    public void logEvent(FragmentEvent fragmentEvent) {
        System.out.println(String.format("%s:%s [%s]", fragmentEvent.getFragmentName(),
                fragmentEvent.getFragmentLocator(), fragmentEvent.getEventDescription()));
    }

}
