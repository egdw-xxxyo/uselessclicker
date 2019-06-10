package org.dikhim.jclicker.eventmanager.filter;


import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class AllEventFilter extends SimpleFilter{
    public AllEventFilter(String id) {
        super(id);
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return true;
    }
}
