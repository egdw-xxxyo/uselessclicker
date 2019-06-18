package org.dikhim.jclicker.eventmanager.listener;

import java.util.Set;
import java.util.TreeSet;

public class PressedKeyHolder {
    private Set<String> pressedKeys = new TreeSet<>();

    public void press(String keyName) {
        pressedKeys.add(keyName);
    }

    public void release(String keyName) {
        pressedKeys.remove(keyName);
    }

    public Set<String> getPressedKeys() {
        return pressedKeys;
    }
}
