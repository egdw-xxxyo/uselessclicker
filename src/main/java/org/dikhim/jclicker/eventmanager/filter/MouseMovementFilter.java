package org.dikhim.jclicker.eventmanager.filter;

import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.MouseMotionEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class MouseMovementFilter extends SimpleFilter{
    public MouseMovementFilter(String id) {
        super(id);
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return event instanceof MouseMotionEvent;
    }
}
