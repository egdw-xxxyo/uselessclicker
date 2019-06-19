package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.MouseButtonEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class MouseButtonFilter extends SimpleFilter{
    public MouseButtonFilter(String id) {
        super(id);
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return event instanceof MouseButtonEvent;
    }
}
