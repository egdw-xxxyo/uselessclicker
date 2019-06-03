package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;

public interface MousePressListener extends EventListener {
    void buttonPressed(MousePressEvent event);
}
