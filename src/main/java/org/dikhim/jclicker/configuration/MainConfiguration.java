package org.dikhim.jclicker.configuration;

import org.dikhim.jclicker.configuration.hotkeys.HotKeys;
import org.dikhim.jclicker.configuration.localization.Localization;
import org.dikhim.jclicker.configuration.recordingparams.RecordingParams;
import org.dikhim.jclicker.configuration.servers.Servers;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class MainConfiguration  {
    private String name;

    private Preferences preferences;

    private HotKeys hotKeys;
    private Localization localization;
    private RecordingParams recordingParams;
    private Servers servers;


    public MainConfiguration(File file, String name) throws FileNotFoundException {
        this.name = name;
        preferences = Preferences.userRoot().node(name);

        FileInputStream fileInputStream = new FileInputStream(file);
        JsonReader jsonReader = Json.createReader(fileInputStream);
        JsonObject jsonObject = jsonReader.readObject();

        loadDefault(jsonObject);
        loadOrSetDefault();
    }

    private void loadDefault(JsonObject jsonObject) {
        hotKeys = new HotKeys(jsonObject.getJsonObject("hotKeys"), name + "/hotKeys");
        localization = new Localization(jsonObject.getJsonObject("localization"), name + "/localization");
        recordingParams = new RecordingParams(jsonObject.getJsonObject("recordingParams"), name + "/recordingParams");
        servers = new Servers(jsonObject.getJsonObject("servers"), name + "/servers");
    }

    public void setDefault() {
        hotKeys.setDefault();
        localization.setDefault();
        recordingParams.setDefault();
        servers.setDefault();
    }

    public void save() {
        hotKeys.save();
        localization.save();
        recordingParams.save();
        servers.save();
    }

    public void loadOrSetDefault() {
        hotKeys.loadOrSetDefault();
        localization.loadOrSetDefault();
        recordingParams.loadOrSetDefault();
        servers.loadOrSetDefault();
    }

    public void flush() {
        try {
            preferences.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }
    //

    public String getName() {
        return name;
    }

    public HotKeys getHotKeys() {
        return hotKeys;
    }

    public Localization getLocalization() {
        return localization;
    }

    public RecordingParams getRecordingParams() {
        return recordingParams;
    }

    public Servers getServers() {
        return servers;
    }
}
