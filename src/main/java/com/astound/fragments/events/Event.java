package com.astound.fragments.events;

public class Event {

	private final String contextName;

	private final String eventDescription;

	private final EventType eventType;

	public Event(String contextName, String eventDescription, EventType eventType) {
		this.contextName = contextName;
		this.eventDescription = eventDescription;
		this.eventType = eventType;
	}

	public String getContextName() {
		return contextName;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public EventType getEventType() {
		return eventType;
	}

	@Override public String toString() {
		return String.format("%s : %s : %s", eventType, contextName, eventDescription);
	}
}