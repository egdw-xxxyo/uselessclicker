package org.dikhim.clickauto.jsengine.events;

import java.util.Set;

public class KeyboardEvent implements Event {
    private String key;
    private Set<String> pressedKeys;
    private String action;
    private long time;


    public KeyboardEvent(String key, Set<String> pressedKeys, String action, long time) {
        this.key = key;
        this.pressedKeys = pressedKeys;
        this.action = action;
        this.time = time;

    }

    public String getKey() {
        return key;
    }

    public Set<String> getPressedKeys() {
        return pressedKeys;
    }

    public String getAction() {
        return action;
    }

    @Override
    public EventType getType() {
        return EventType.KEYBOARD;
    }

    @Override
    public long getTime() {
        return time;
    }
}
