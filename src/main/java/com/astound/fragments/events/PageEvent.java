package com.astound.fragments.events;

public class PageEvent {

    private final String contextName;

    private final String eventDescription;

    private final PageEventType pageEventType;

    public PageEvent(String contextName, String eventDescription, PageEventType pageEventType) {
        this.contextName = contextName;
        this.eventDescription = eventDescription;
        this.pageEventType = pageEventType;
    }

    public String getContextName() {
        return contextName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public PageEventType getPageEventType() {
        return pageEventType;
    }

    @Override public String toString() {
        return String.format("%s : %s : %s", pageEventType, contextName, eventDescription);
    }
}