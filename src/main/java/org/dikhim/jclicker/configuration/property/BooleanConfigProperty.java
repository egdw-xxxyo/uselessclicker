package org.dikhim.jclicker.configuration.property;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class BooleanConfigProperty implements ConfigProperty<Boolean> {
    private final String name;
    private final Preferences preferences;
    private final boolean defaultValue;
    private final BooleanProperty currentValue;

    public BooleanConfigProperty(String propertyName, boolean defaultValue, Preferences preferences) {
        this.name = propertyName;
        this.preferences = preferences;
        this.defaultValue = defaultValue;
        this.currentValue = new SimpleBooleanProperty(defaultValue);
        resetToSaved();
    }

    @Override
    public void save() {
        preferences.putBoolean(name, currentValue.get());
        try {
            preferences.flush();
        } catch (BackingStoreException ignored) {
        }
    }

    @Override
    public void resetToDefault() {
        currentValue.set(defaultValue);
    }

    @Override
    public void resetToSaved() {
        currentValue.set(preferences.getBoolean(name, defaultValue));
    }

    @Override
    public Boolean get() {
        return currentValue.get();
    }

    @Override
    public void set(Boolean value) {
        currentValue.set(value);
    }

    @Override
    public BooleanProperty getProperty() {
        return currentValue;
    }

    @Override
    public String getName() {
        return name;
    }
}
