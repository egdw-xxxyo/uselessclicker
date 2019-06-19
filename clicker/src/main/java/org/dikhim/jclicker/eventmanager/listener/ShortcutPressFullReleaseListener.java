package org.dikhim.jclicker.eventmanager.listener;

import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class ShortcutPressFullReleaseListener extends SimpleListener implements KeyListener {
    private Set<String> pressedKeys = new TreeSet<>();
    private Set<String> shortcutKeys = new TreeSet<>();
    private Runnable onFire;
    private boolean messedUp = false;
    private boolean fired = false;


    public ShortcutPressFullReleaseListener(String id, String keys, Runnable onFire) {
        super(id);
        this.onFire = onFire;

        shortcutKeys.addAll(Arrays.asList(keys.split(" ")));
    }

    public ShortcutPressFullReleaseListener(String id, Runnable onFire, StringProperty keysProperty) {
        super(id);
        this.onFire = onFire;

        shortcutKeys.addAll(Arrays.asList(keysProperty.getValue().split(" ")));

        keysProperty.addListener((observable, oldValue, newValue) -> {
            shortcutKeys.clear();
            shortcutKeys.addAll(Arrays.asList(newValue.split(" ")));
        });
    }

    @Override
    public void keyPressed(KeyPressEvent event) {
        String key = event.getKey();

        if (pressedKeys.isEmpty()) {
            if (shortcutKeys.contains(key)) {
                pressedKeys.add(event.getKey());
                messedUp = false;
            } else {
                messedUp = true;
            }
        } else {
            if (shortcutKeys.contains(key)) {
                pressedKeys.add(event.getKey());
            } else {
                messedUp = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyReleaseEvent event) {
        String key = event.getKey();

        if (!pressedKeys.isEmpty()) {
            if (pressedKeys.equals(shortcutKeys) && !messedUp) {
                fired = true;
            }
            pressedKeys.remove(key);
            
            if (pressedKeys.isEmpty()) {
                messedUp = false;
                check();
                fired = false;
            }
        }
    }

    private void check() {
        if (pressedKeys.isEmpty() && fired) {
            onFire.run();
        }
    }
}