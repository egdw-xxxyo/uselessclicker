package org.dikhim.jclicker.global;

import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;
import org.dikhim.jclicker.eventmanager.listener.PressedKeyHolder;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Set;

public class Keyboard {
    private final PressedKeyHolder pressedKeyHolder = new PressedKeyHolder();

    public Keyboard(EventManager eventManager) {
        eventManager.addListener(new KeyPressReleaseListener("keyboard.pressed.keys","ANY",
                event -> pressedKeyHolder.press(event.getKey()),
                event -> pressedKeyHolder.release(event.getKey())));
    }


    public synchronized boolean isPressed(Set<String> keys) {

        return pressedKeyHolder.getPressedKeys().containsAll(keys);
    }

    public synchronized boolean isCapsLockLocked() {
        return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
    }

    public synchronized boolean isNumLockLocked() {
        return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
    }

    public synchronized boolean isScrollLockLocked() {
        return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
    }
}
