package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class PrefixFilter implements Filter {

    private String ignoredPrefix;

    public PrefixFilter(String ignoredPrefix) {
        this.ignoredPrefix = ignoredPrefix;
    }

    @Override
    public boolean ignored(String key, Event event, EventListener eventListener) {
        return key.startsWith(ignoredPrefix);
    }
}
