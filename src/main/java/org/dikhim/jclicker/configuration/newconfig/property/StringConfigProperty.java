package org.dikhim.jclicker.configuration.newconfig.property;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class StringConfigProperty implements ConfigProperty<String> {
    private final String name;
    private final Preferences preferences;
    private final String defaultValue;
    private final StringProperty currentValue;

    public StringConfigProperty(String propertyName, String defaultValue, Preferences preferences) {
        this.name = propertyName;
        this.preferences = preferences;
        this.defaultValue = defaultValue;
        this.currentValue = new SimpleStringProperty(defaultValue);
        resetToSaved();
    }

    @Override
    public void save() {
        preferences.put(name, currentValue.get());
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
        currentValue.set(preferences.get(name, defaultValue));
    }

    @Override
    public String get() {
        return currentValue.get();
    }

    @Override
    public void set(String value) {
        currentValue.set(value);
    }

    @Override
    public StringProperty getProperty() {
        return currentValue;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StringConfigProperty{" +
                "name='" + name + '\'' +
                ", preferences=" + preferences +
                ", defaultValue='" + defaultValue + '\'' +
                ", currentValue=" + currentValue +
                '}';
    }
}
