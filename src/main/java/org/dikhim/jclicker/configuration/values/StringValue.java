package org.dikhim.jclicker.configuration.values;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.prefs.Preferences;

public class StringValue {
    private String name;
    private String defaultValue;
    private StringProperty value = new SimpleStringProperty("");

    public StringValue(String name, String defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public void setDefault() {
        value.set(defaultValue);
    }

    public void save(Preferences preferences) {
        preferences.put(name, value.get());
    }

    public void loadOrSetDefault(Preferences preferences){
        value.set(preferences.get(name, defaultValue));
    }

    public String getName() {
        return name;
    }

    public String getDefault() {
        return defaultValue;
    }

    public String get() {
        return value.get();
    }

    public void set(String value){
        this.value.set(value);}

    public StringProperty valueProperty() {
        return value;
    }
}
