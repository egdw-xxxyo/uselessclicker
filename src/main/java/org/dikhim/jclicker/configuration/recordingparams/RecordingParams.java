package org.dikhim.jclicker.configuration.recordingparams;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class RecordingParams {
    private String name;

    private Preferences preferences;

    private Combined combined;
    private Global global;

    public RecordingParams(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        combined = new Combined(jsonObject.getJsonObject("combined"), name + "/combined");
        global = new Global(jsonObject.getJsonObject("global"), name + "/global");
    }

    public void setDefault() {
        combined.setDefault();
        global.setDefault();
    }

    public void save() {
        combined.save();
        global.save();
    }

    public void loadOrSetDefault() {
        combined.loadOrSetDefault();
        global.loadOrSetDefault();
    }
    //

    public String getName() {
        return name;
    }

    public Combined getCombined() {
        return combined;
    }

    public Global getGlobal() {
        return global;
    }
}
