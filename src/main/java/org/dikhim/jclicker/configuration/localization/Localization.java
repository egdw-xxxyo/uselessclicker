package org.dikhim.jclicker.configuration.localization;

import org.dikhim.jclicker.configuration.values.StringValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Localization {
    private String path;
    private String name;
    private Preferences preferences;

    private StringValue applicationLanguage;
    private Languages languages;


    public Localization(JsonObject jsonObject, String path, String name) {
        this.path = path;
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        applicationLanguage = new StringValue(jsonObject.getString("applicationLanguage"), name + "/applicationLanguage");
        languages = new Languages(jsonObject.getJsonObject("languages"), path + "/" + name, name);
    }

    public void setDefault() {
        applicationLanguage.setDefault();
        languages.setDefault();
    }

    public void save() {
        applicationLanguage.save(preferences);
        languages.save();
    }

    public void loadOrSetDefault() {
        applicationLanguage.loadOrSetDefault(preferences);
        languages.loadOrSetDefault();
    }

    //
    public String getName() {
        return name;
    }

    public StringValue getApplicationLanguage() {
        return applicationLanguage;
    }

    public Languages getLanguages() {
        return languages;
    }

    public String getPath() {
        return path;
    }
}
