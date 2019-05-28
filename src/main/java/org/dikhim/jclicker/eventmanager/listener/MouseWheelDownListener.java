package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;

public interface MouseWheelDownListener extends EventListener {
    void wheeledDown(MouseWheelDownEvent event);
}
