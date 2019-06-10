package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.KeyboardEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class KeyboardFilter extends SimpleFilter{
    public KeyboardFilter(String id) {
        super(id);
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return event instanceof KeyboardEvent;
    }
}
