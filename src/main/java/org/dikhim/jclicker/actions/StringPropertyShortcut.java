package org.dikhim.jclicker.actions;

import javafx.beans.property.StringProperty;

import java.util.Set;

public class StringPropertyShortcut implements Shortcut {
    private StringProperty stringProperty;

    public StringPropertyShortcut(StringProperty stringProperty) {
        this.stringProperty = stringProperty;
    }

    @Override
    public boolean isEqual(Set<String> shortcut) {
        Set<String> keys = getKeySet(stringProperty.get());
        return keys.equals(shortcut);
    }

    @Override
    public boolean containsIn(Set<String> shortcut) {
        Set<String> keys = getKeySet(stringProperty.get());
        if(keys.isEmpty())return true;
        return shortcut.containsAll(keys);
    }

    @Override
    public Set<String> getKeys() {
        return null;
    }

    @Override
    public void setKeys(String keys) {
        stringProperty.setValue(keys);
    }
}
