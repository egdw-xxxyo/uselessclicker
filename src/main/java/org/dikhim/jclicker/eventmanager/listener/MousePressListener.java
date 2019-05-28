package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;

public interface MousePressListener extends EventListener {
    void mousePressed(MousePressEvent event);
}
