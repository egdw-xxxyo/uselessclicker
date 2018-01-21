package org.dikhim.jclicker.configuration.servers;

import org.dikhim.jclicker.configuration.values.IntegerValue;

import javax.json.JsonObject;
import java.util.prefs.Preferences;

public class Server {
    String name;

    private Preferences preferences;

    private IntegerValue port;

    public Server(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
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

    public String getName() {
        return name;
    }

    public IntegerValue getFixedRateValue() {
        return port;
    }
}
