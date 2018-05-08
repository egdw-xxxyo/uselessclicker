package org.dikhim.jclicker.configuration.localization;

import org.dikhim.jclicker.configuration.values.StringValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Language {
    private String name;

    private Preferences preferences;

    private StringValue nativeName;
    private StringValue id;

    public Language(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        nativeName = new StringValue("nativeName", jsonObject.getString("nativeName"));
        id = new StringValue("id", jsonObject.getString("id"));
    }


    public void setDefault() {
        id.setDefault();
        nativeName.setDefault();
    }

    public void save() {
        id.save(preferences);
        nativeName.save(preferences);
    }

    public void loadOrSetDefault() {
        id.loadOrSetDefault(preferences);
        nativeName.loadOrSetDefault(preferences);
    }

    //

    public String getName() {
        return name;
    }

    public StringValue getId() {
        return id;
    }

    public StringValue getNativeName() {
        return nativeName;
    }
}
