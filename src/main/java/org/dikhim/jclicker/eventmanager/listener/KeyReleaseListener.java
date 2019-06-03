package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;

public interface KeyReleaseListener extends EventListener {
    void keyReleased(KeyReleaseEvent event);
}
