package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;

public interface MouseWheelUpListener extends EventListener {
    void wheeledUp(MouseWheelUpEvent event);
}
