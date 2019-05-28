package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public interface Filter {
    boolean ignored(String key, Event event, EventListener eventListener);
}
