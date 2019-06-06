package org.dikhim.jclicker.eventmanager.listener;

import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class ShortcutPressListener extends SimpleListener implements KeyListener {
    private Set<String> pressedKeys = new TreeSet<>();
    private Set<String> shortcutKeys = new TreeSet<>();
    private Runnable onFire;

    public ShortcutPressListener(String id, Runnable onFire, String... keys) {
        super(id);

        this.onFire = onFire;
        shortcutKeys.addAll(Arrays.asList(keys));
    }

    public ShortcutPressListener(String id, Runnable onFire, StringProperty stringProperty) {
        super(id);
        this.onFire = onFire;
        shortcutKeys.clear();
        shortcutKeys.addAll(Arrays.asList(stringProperty.getValue().split(" ")));
        
        stringProperty.addListener((observable, oldValue, newValue) -> {
            shortcutKeys.clear();
            shortcutKeys.addAll(Arrays.asList(newValue.split(" ")));
        });
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