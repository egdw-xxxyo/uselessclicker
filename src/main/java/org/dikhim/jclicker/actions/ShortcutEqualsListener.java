package org.dikhim.jclicker.actions;

import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.actions.events.KeyboardEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ShortcutEqualsListener implements KeyboardListener {
    private String name;
    private String keys;
    private Shortcut shortcut;
    private Consumer<KeyboardEvent> handler;
    private String action;

    public ShortcutEqualsListener() {
    }

    public ShortcutEqualsListener(String name, String shortcut, String action, Consumer<KeyboardEvent> handler) {
        this.name = name;
        this.keys = shortcut;
        this.shortcut = new StringShortcut(shortcut);
        this.handler = handler;
        this.action = action;
    }

    @Override
    public void setKeys(String keys) {
        this.keys = keys;
        shortcut.setKeys(keys);
    }

    @Override
    public String getKeys() {
        return keys;
    }

    public void setShortcut(Shortcut shortcut) {
        this.shortcut = shortcut;
    }

    public void fire(KeyboardEvent keyboardEvent) {
        if (!action.equals(keyboardEvent.getAction())) return;
        if (shortcut.isEqual(keyboardEvent.getPressedKeys())) handler.accept(keyboardEvent);
    }

    /**
     * @return the handler
     */
    public Consumer<KeyboardEvent> getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(Consumer<KeyboardEvent> handler) {
        this.handler = handler;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    public void setAction(String action) {
        this.action = action;
    }
}
