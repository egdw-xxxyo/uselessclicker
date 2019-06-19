package org.dikhim.jclicker.configuration.property;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class IntegerConfigProperty implements ConfigProperty<Integer> {
    private final String name;
    private final Preferences preferences;
    private final int defaultValue;
    private final IntegerProperty currentValue;

    public IntegerConfigProperty(String propertyName, int defaultValue, Preferences preferences) {
        this.name = propertyName;
        this.preferences = preferences;
        this.defaultValue = defaultValue;
        this.currentValue = new SimpleIntegerProperty(defaultValue);
        resetToSaved();
    }

    @Override
    public void save() {
        preferences.putInt(name, currentValue.get());
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
        currentValue.set(preferences.getInt(name, defaultValue));
    }

    @Override
    public Integer get() {
        return currentValue.get();
    }

    @Override
    public void set(Integer value) {
        currentValue.set(value);
    }

    @Override
    public IntegerProperty getProperty() {
        return currentValue;
    }

    @Override
    public String getName() {
        return name;
    }
}
