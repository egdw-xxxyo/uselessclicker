package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;

public class KeyPressReleaseListener implements KeyListener {
    private String key;
    private Runnable onPress;
    private Runnable onRelease;

    public KeyPressReleaseListener(String key, Runnable onPress, Runnable onRelease) {
        this.key = key;
        this.onPress = onPress;
        this.onRelease = onRelease;
    }

    @Override
    public void keyPressed(KeyPressEvent event) {
        if (key.toUpperCase().equals(event.getKey())) onPress.run();
    }

    @Override
    public void keyReleased(KeyReleaseEvent event) {
        if (key.toUpperCase().equals(event.getKey())) onRelease.run();
    }
}
