package org.dikhim.jclicker.configuration.values;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.prefs.Preferences;

public class IntegerValue {
    private String name;
    private int defaultValue;
    private IntegerProperty value = new SimpleIntegerProperty(0);

    public IntegerValue(String name, int defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public void setDefault() {
        value.set(defaultValue);
    }

    public void save(Preferences preferences) {
        preferences.putInt(name, value.get());
    }

    public void loadOrSetDefault(Preferences preferences){
        value.set(preferences.getInt(name, defaultValue));
    }

    public String getName() {
        return name;
    }

    public int getDefault() {
        return defaultValue;
    }

    public int get() {
        return value.get();
    }

    public void set(int value){
        this.value.set(value);}

    public IntegerProperty valueProperty() {
        return value;
    }


}
