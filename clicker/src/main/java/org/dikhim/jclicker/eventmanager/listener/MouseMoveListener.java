package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MouseMoveEvent;

public interface MouseMoveListener extends EventListener {
    void mouseMoved(MouseMoveEvent event);
}
