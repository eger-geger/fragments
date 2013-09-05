package com.astound.fragments.utils;

import com.astound.fragments.events.PageEvent;
import com.google.common.eventbus.Subscribe;

public class PageEventLogger {

    @Subscribe public void handle(PageEvent pageEvent) {
        System.out.println(pageEvent.toString());
    }

}
