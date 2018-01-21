package org.dikhim.jclicker.configuration.values;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.prefs.Preferences;

public class BooleanValue {
    private String name;
    private boolean defaultValue;
    private BooleanProperty value = new SimpleBooleanProperty(false);

    public BooleanValue(String name, boolean defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public void setDefault() {
        value.set(defaultValue);
    }

    public void save(Preferences preferences) {
        preferences.putBoolean(name, value.get());
    }

    public void loadOrSetDefault(Preferences preferences){
        value.set(preferences.getBoolean(name, defaultValue));
    }

    public String getName() {
        return name;
    }

    public boolean getDefault() {
        return defaultValue;
    }

    public boolean get() {
        return value.get();
    }

    public void set(boolean value){
        this.value.set(value);}

    public BooleanProperty valueProperty() {
        return value;
    }

}
