package org.dikhim.jclicker.configuration.servers;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class Servers {
    private String path;
    private String name;

    private Preferences preferences;
    private final List<ServerConfig> serverList = new ArrayList<>();

    public Servers(JsonObject jsonObject, String path, String name) {
        this.path = path;
        this.name = name;
        preferences = Preferences.userRoot().node(path);
        loadDefault(jsonObject);
    }

    private void loadDefault(JsonObject jsonObject) {
        jsonObject.keySet().forEach(key ->
                serverList.add(new ServerConfig(jsonObject.getJsonObject(key), path + "/" + key, key)));
    }

    public void setDefault() {
        serverList.forEach(ServerConfig::setDefault);
    }

    public void save() {
        serverList.forEach(ServerConfig::save);
    }

    public void loadOrSetDefault() {
        serverList.forEach(ServerConfig::loadOrSetDefault);
    }

    //
    public String getPath() {
        return path;
    }

    public List<ServerConfig> getServerList() {
        return serverList;
    }

    public ServerConfig getServer(String name) {
        for (ServerConfig s : serverList) {
            if (s.getName().equals(name)) return s;
        }
        throw new IllegalArgumentException("unknown parameter config '" + name + "' in '" + path);
    }
}
