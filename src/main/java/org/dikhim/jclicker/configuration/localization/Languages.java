package org.dikhim.jclicker.configuration.localization;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Languages {
    private String path;
    private String name;

    private Preferences preferences;
    private final List<Language> languages = new ArrayList<>();

    public Languages(JsonObject jsonObject, String path, String name) {
        this.path = path;
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        jsonObject.keySet().forEach(key ->
                languages.add(new Language(jsonObject.getJsonObject(key), path + "/" + key, key)));
    }

    public void setDefault() {
        languages.forEach(Language::setDefault);
    }

    public void save() {
        languages.forEach(Language::save);
    }

    public void loadOrSetDefault() {
        languages.forEach(Language::loadOrSetDefault);
    }

    //
    public String getName() {
        return name;
    }

    public List<Language> getLanguageList() {
        return languages;
    }

    public String getPath() {
        return path;
    }
}
