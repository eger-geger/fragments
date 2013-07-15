package com.astound.fragments.events;

import com.astound.fragments.elements.Fragment;

public class FragmentEvent {

    private String fragmentName;

    private String fragmentLocator;

    private final String eventDescription;

    public FragmentEvent(Fragment fragment, String eventDescription) {
        this.fragmentName = fragment.getName();
        this.fragmentLocator = fragment.getLocator();
        this.eventDescription = eventDescription;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public String getFragmentLocator() {
        return fragmentLocator;
    }

    public String getEventDescription() {
        return eventDescription;
    }

}