package org.dikhim.jclicker.actions;

import org.dikhim.jclicker.actions.events.KeyboardEvent;

import java.util.function.Consumer;

public class KeyListener implements KeyboardListener {
    private String name;
    private String key;
    private String action;
    private Consumer<KeyboardEvent> handler;

    public KeyListener(String name, String key, String action, Consumer<KeyboardEvent> handler) {
        this.name = name;
        this.key = key;
        this.action = action;
        this.handler = handler;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setKeys(String keys) {
        key = keys;
    }

    @Override
    public String getKeys() {
        return key;
    }

    @Override
    public Consumer<KeyboardEvent> getHandler() {
        return handler;
    }

    @Override
    public void setHandler(Consumer<KeyboardEvent> handler) {
        this.handler = handler;
    }

    @Override
    public void fire(KeyboardEvent keyboardEvent) {
        if(!keyboardEvent.getAction().equals(action))return;
        if(key.isEmpty()){
            handler.accept(keyboardEvent);
            return;
        }
        if (!keyboardEvent.getKey().equals(key)) return;
        handler.accept(keyboardEvent);
    }
}
