package org.dikhim.jclicker.configuration.servers;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Servers {
    private String path;
    private String name;

    private Preferences preferences;
    private final List<Server> serverList = new ArrayList<>();

    public Servers(JsonObject jsonObject, String path, String name) {
        this.path = path;
        this.name = name;
        preferences = Preferences.userRoot().node(path);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        jsonObject.keySet().forEach(key ->
                serverList.add(new Server(jsonObject.getJsonObject(key), path + "/" + key, key)));
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
    public String getPath() {
        return path;
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public Server getServer(String name) {
        for (Server s : serverList) {
            if (s.getName().equals(name)) return s;
        }
        throw new IllegalArgumentException("unknown parameter config '" + name + "' in '" + path);
    }
}
