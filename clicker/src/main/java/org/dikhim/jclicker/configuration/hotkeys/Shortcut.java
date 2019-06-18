package org.dikhim.jclicker.configuration.hotkeys;

import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.configuration.property.SimpleConfigElement;
import org.dikhim.jclicker.configuration.property.StringConfigProperty;

import java.util.prefs.Preferences;

public class Shortcut extends SimpleConfigElement {
    private final StringConfigProperty keys;
    private final String category;

    public Shortcut(String name, String defaultKeys, String category, Preferences preferences) {
        super(name, preferences);

        this.keys = new StringConfigProperty("keys", defaultKeys, getPreferences());
        this.category = category;
    }

    @Override
    public void save() {
        keys.save();
    }

    @Override
    public void resetToDefault() {
        keys.resetToDefault();
    }

    @Override
    public void resetToSaved() {
        keys.resetToSaved();
    }

    public String getKeys() {
        return keys.get();
    }

    public StringProperty keysProperty() {
        return keys.getProperty();
    }
    
    public void setKeys(String keys) {
        this.keys.set(keys);
    }

    public String getCategory() {
        return category;
    }
}
