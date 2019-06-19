package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.MouseEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class MouseFilter extends SimpleFilter{
    public MouseFilter(String id) {
        super(id);
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return event instanceof MouseEvent;

    }
}
