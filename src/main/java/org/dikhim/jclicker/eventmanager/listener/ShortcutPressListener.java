package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class ShortcutPressListener implements KeyListener {
    private Set<String> pressedKeys =  new TreeSet<>();
    private Set<String> shortcutKeys = new TreeSet<>();
    private Runnable onFire;

    public ShortcutPressListener(Runnable onFire, String ... keys) {
        this.onFire = onFire;
        shortcutKeys.addAll(Arrays.asList(keys));
    }

    @Override
    public void keyPressed(KeyPressEvent event) {
        pressedKeys.add(event.getKey());
        check();
    }

    @Override
    public void keyReleased(KeyReleaseEvent event) {
        pressedKeys.remove(event.getKey());
    }

    private void check() {
        if (pressedKeys.equals(shortcutKeys)) {
            onFire.run();
        }
    }
}