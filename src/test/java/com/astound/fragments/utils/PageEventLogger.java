package com.astound.fragments.utils;

import com.astound.fragments.events.Event;
import com.google.common.eventbus.Subscribe;

public class PageEventLogger {

	@Subscribe public void handle(Event event) {
		System.out.println(event.toString());
	}

}
