package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.KeyboardEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class KeyboardFilter implements Filter {
    @Override
    public boolean ignored(String key, Event event, EventListener eventListener) {
        return event instanceof KeyboardEvent;
    }
}
