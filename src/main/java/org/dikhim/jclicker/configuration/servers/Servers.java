package org.dikhim.jclicker.configuration.servers;

import org.dikhim.jclicker.configuration.localization.Language;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Servers {

    private String name;
    private Preferences preferences;
    private final List<Server> serverList = new ArrayList<>();

    public Servers(JsonObject jsonObject, String name) {
        this.name = name;
        preferences = Preferences.userRoot().node(name);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        jsonObject.keySet().forEach(key ->
                serverList.add(new Server(jsonObject.getJsonObject(key), name + "/" + key)));
    }

    public void setDefault() {
        serverList.forEach(Server::setDefault);
    }

    public void save() {
        serverList.forEach(Server::save);
    }

    public void loadOrSetDefault() {
        serverList.forEach(Server::loadOrSetDefault);
    }

    //
    public String getName() {
        return name;
    }

    public List<Server> getServerList() {
        return serverList;
    }
}
