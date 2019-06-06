package org.dikhim.jclicker.eventmanager.filter;


import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class AllEventFilter implements Filter {
    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return true;
    }
}
