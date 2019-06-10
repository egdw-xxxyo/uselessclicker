package org.dikhim.jclicker.eventmanager.filter;

import javafx.beans.property.BooleanProperty;
import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.event.MouseEvent;
import org.dikhim.jclicker.eventmanager.listener.EventListener;

public class MousePrefixFilter extends SimpleFilter{
    private String ignoredPrefix;

    public MousePrefixFilter(String id, String ignoredPrefix) {
        super(id);
        this.ignoredPrefix = ignoredPrefix;
    }

    @Override
    public boolean ignored(EventListener eventListener, Event event) {
        return (event instanceof MouseEvent && eventListener.getId().startsWith(ignoredPrefix));
    }
}
