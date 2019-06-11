package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.MouseWheelEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class MouseWheelFilter extends SimpleFilter{
    public MouseWheelFilter(String id) {
        super(id);
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return event instanceof MouseWheelEvent;
    }
}
