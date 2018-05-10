package org.dikhim.jclicker.actions;

import org.dikhim.jclicker.actions.events.KeyboardEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ShortcutEqualsListener implements KeyboardListener {
    private String name;
    private Set<StringShortcut> stringShortcuts = new HashSet<>();
    private Consumer<KeyboardEvent> handler;
    private String action;

    public ShortcutEqualsListener(String name, String shortcut, String action, Consumer<KeyboardEvent> handler) {
        this.name = name;
        stringShortcuts.add(new StringShortcut(shortcut));
        this.handler = handler;
        this.action = action;
    }

    public void addShortcut(String shortcut) {
        stringShortcuts.add(new StringShortcut(shortcut));
    }

    public void fire(KeyboardEvent keyboardEvent) {
        if(!action.equals(keyboardEvent.getAction()))return;
        for (StringShortcut sh : stringShortcuts) {
            if (sh.isEqual(keyboardEvent.getPressedKeys())) handler.accept(keyboardEvent);
        }
    }

    /**
     * @return the shortcuts
     */
    public Set<StringShortcut> getStringShortcuts() {
        return stringShortcuts;
    }

    /**
     * @param stringShortcuts the shortcuts to set
     */
    public void setStringShortcuts(Set<StringShortcut> stringShortcuts) {
        this.stringShortcuts = stringShortcuts;
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
    public void setHandler(Consumer<KeyboardEvent>  handler) {
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

}
