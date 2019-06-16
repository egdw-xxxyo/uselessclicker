package org.dikhim.jclicker.configuration.servers;

import javafx.beans.property.IntegerProperty;
import org.dikhim.jclicker.configuration.property.SimpleConfigElement;
import org.dikhim.jclicker.configuration.property.IntegerConfigProperty;

import java.util.prefs.Preferences;

public class Server extends SimpleConfigElement {
    private final IntegerConfigProperty port;

    public Server(String name, Preferences preferences) {
        super(name, preferences);

        port = new IntegerConfigProperty("port", 5001, getPreferences());
    }


    @Override
    public void save() {
        port.save();
    }

    @Override
    public void resetToDefault() {
        port.resetToDefault();
    }

    @Override
    public void resetToSaved() {
        port.resetToSaved();
    }

    public int getPort() {
        return port.get();
    }
    
    public IntegerProperty portProperty() {
        return port.getProperty();
    }
    
    public void setPort(int port) {
        this.port.set(port);
    }
}
