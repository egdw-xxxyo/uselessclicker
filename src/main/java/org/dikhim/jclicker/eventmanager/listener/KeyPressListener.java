package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;

public interface KeyPressListener extends EventListener {
    void keyPressed(KeyPressEvent event);
}
