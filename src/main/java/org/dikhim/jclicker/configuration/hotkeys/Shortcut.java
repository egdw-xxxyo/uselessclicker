package org.dikhim.jclicker.configuration.hotkeys;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.configuration.values.IntegerValue;
import org.dikhim.jclicker.configuration.values.StringValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Shortcut {
    private String name;

    private Preferences preferences;

    private StringValue keys;

    public Shortcut(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        keys = new StringValue("keys", jsonObject.getString("keys"));
    }

    public void setDefault() {
        keys.setDefault();
    }

    public void save() {
        keys.save(preferences);
    }

    public void loadOrSetDefault() {
        keys.loadOrSetDefault(preferences);
    }

    public String getName() {
        return name;
    }

    public StringValue getKeys() {
        return keys;
    }
}
