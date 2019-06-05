package org.dikhim.jclicker.configuration.newconfig.servers;

import org.dikhim.jclicker.configuration.newconfig.property.SimpleConfigElement;

import java.util.prefs.Preferences;

public class Servers extends SimpleConfigElement {
    private final Server socket;
    private final Server http;

    public Servers(String name, Preferences preferences) {
        super(name, preferences);

        socket = new Server("socket", getPreferences());
        http = new Server("http", getPreferences());
    }

    @Override
    public void save() {
        socket.save();
        http.save();
    }

    @Override
    public void resetToDefault() {
        socket.resetToDefault();
        http.resetToDefault();
    }

    @Override
    public void resetToSaved() {
        socket.resetToSaved();
        http.resetToSaved();
    }

    public Server socket() {
        return socket;
    }

    public Server http() {
        return http;
    }
}
