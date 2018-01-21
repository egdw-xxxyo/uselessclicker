package org.dikhim.jclicker.configuration.localization;

import org.dikhim.jclicker.configuration.values.StringValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Localization {

    private String name;
    private Preferences preferences;

    private StringValue defaultLanguage;
    private StringValue selectedLanguage;
    private Languages languages;


    public Localization(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        defaultLanguage = new StringValue(jsonObject.getString("defaultLanguage"), name + "/defaultLanguage");
        selectedLanguage = new StringValue(jsonObject.getString("selectedLanguage"), name + "/selectedLanguage");
        languages = new Languages(jsonObject.getJsonObject("languages"), name + "/languages");
    }

    public void setDefault() {
        defaultLanguage.setDefault();
        selectedLanguage.setDefault();
        languages.setDefault();
    }

    public void save() {
        defaultLanguage.save(preferences);
        selectedLanguage.save(preferences);
        languages.save();
    }

    public void loadOrSetDefault() {
        defaultLanguage.loadOrSetDefault(preferences);
        selectedLanguage.loadOrSetDefault(preferences);
        languages.loadOrSetDefault();
    }

    //
    public String getName() {
        return name;
    }

    public StringValue getDefaultLanguage() {
        return defaultLanguage;
    }

    public StringValue getSelectedLanguage() {
        return selectedLanguage;
    }

    public Languages getLanguages() {
        return languages;
    }
}
