package org.dikhim.jclicker.actions;

import java.util.HashSet;
import java.util.Set;

public interface Shortcut {
    boolean isEqual(Set<String> shortcut);

    boolean containsIn(Set<String> shortcut);

    Set<String> getKeys();

    void setKeys(String keys);
    
    default Set<String> getKeySet(String keys) {
        String[] strKeys = keys.split(" ");
        Set<String> keySet = new HashSet<>();
        for (String s : strKeys) {
            if (!s.equals(""))
                keySet.add(s);
        }
        return keySet;
    }
}
