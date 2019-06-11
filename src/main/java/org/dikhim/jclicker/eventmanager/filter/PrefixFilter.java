package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class PrefixFilter extends SimpleFilter {
    private String ignoredPrefix;

    public PrefixFilter(String id, String ignoredPrefix) {
        super(id);
        this.ignoredPrefix = ignoredPrefix;
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return eventListener.getId().startsWith(ignoredPrefix);
    }
}
