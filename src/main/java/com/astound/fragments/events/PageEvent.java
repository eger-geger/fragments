package com.astound.fragments.events;

public class PageEvent {

    public static enum EventType {ELEMENT_EVENT, PAGE_EVENT, USER_EVENT}

    private final String contextName;

    private final String eventDescription;

    private final EventType eventType;

    public PageEvent(String contextName, String eventDescription, EventType eventType) {
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