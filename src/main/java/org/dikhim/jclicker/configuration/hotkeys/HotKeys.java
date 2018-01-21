package org.dikhim.jclicker.configuration.hotkeys;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class HotKeys {


    private String name;
    private Preferences preferences;
    private final List<Shortcut> shortcutList = new ArrayList<>();

    public HotKeys(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        jsonObject.keySet().forEach(key ->
                shortcutList.add(new Shortcut(jsonObject.getJsonObject(key), name + "/" + key)));
    }

    public void setDefault() {
        shortcutList.forEach(Shortcut::setDefault);
    }

    public void save() {
        shortcutList.forEach(Shortcut::save);
    }

    public void loadOrSetDefault() {
        shortcutList.forEach(Shortcut::loadOrSetDefault);
    }

    //
    public String getName() {
        return name;
    }

    public List<Shortcut> getShortcutList() {
        return shortcutList;
    }
}
