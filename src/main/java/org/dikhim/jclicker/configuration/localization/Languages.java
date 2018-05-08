package org.dikhim.jclicker.configuration.localization;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Languages {


    private String name;
    private Preferences preferences;
    private final List<Language> langugeList = new ArrayList<>();

    public Languages(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        jsonObject.keySet().forEach(key ->
                langugeList.add(new Language(jsonObject.getJsonObject(key), name + "/" + key)));
    }

    public void setDefault() {
        langugeList.forEach(Language::setDefault);
    }

    public void save() {
        langugeList.forEach(Language::save);
    }

    public void loadOrSetDefault() {
        langugeList.forEach(Language::loadOrSetDefault);
    }

    //
    public String getName() {
        return name;
    }

    public List<Language> getLanguageList() {
        return langugeList;
    }
}
