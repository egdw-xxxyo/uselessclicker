package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;

public interface MouseReleaseListener extends EventListener {
    void buttonReleased(MouseReleaseEvent event);
}
