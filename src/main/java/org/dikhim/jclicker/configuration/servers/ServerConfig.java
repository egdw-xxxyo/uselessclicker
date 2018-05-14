package org.dikhim.jclicker.configuration.servers;

import org.dikhim.jclicker.configuration.values.IntegerValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class ServerConfig {
    private String path;
    private String name;

    private Preferences preferences;

    private IntegerValue port;


    public ServerConfig(JsonObject jsonObject, String path, String name) {
        this.path = path;
        this.name = name;
        preferences = Preferences.userRoot().node(path);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        port = new IntegerValue("port", jsonObject.getInt("port"));
    }


    public void setDefault() {
        port.setDefault();
    }

    public void save() {
        port.save(preferences);
    }

    public void loadOrSetDefault() {
        port.loadOrSetDefault(preferences);
    }

    //

    public String getPath() {
        return path;
    }

    public IntegerValue getPort() {
        return port;
    }
    
    public String getName() {
        return name;
    }
}
