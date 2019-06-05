package org.dikhim.jclicker.configuration.newconfig.property;

import java.util.prefs.Preferences;

public abstract class SimpleConfigElement implements ConfigElement {
    private String name;
    private Preferences preferences;

    public SimpleConfigElement(String name, Preferences preferences) {
        this.name = name;
        this.preferences = preferences.node(name);
    }

    @Override
    public String getName() {
        return name;
    }
    
    protected Preferences getPreferences() {
        return preferences;
    }
}
