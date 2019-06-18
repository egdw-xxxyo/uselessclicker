package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.KeyboardEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class KeyboardPrefixFilter extends SimpleFilter{
    private String ignoredPrefix;

    public KeyboardPrefixFilter(String id, String ignoredPrefix) {
        super(id);
        this.ignoredPrefix = ignoredPrefix;
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return (event instanceof KeyboardEvent && eventListener.getId().startsWith(ignoredPrefix));
    }
}
