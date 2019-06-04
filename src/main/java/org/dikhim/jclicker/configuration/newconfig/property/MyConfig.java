package org.dikhim.jclicker.configuration.newconfig.property;

import java.util.prefs.Preferences;

public class MyConfig {
    private BooleanConfigProperty isOn;

    public MyConfig() {
        isOn = new BooleanConfigProperty("isOn",false, Preferences.userRoot().node("/"));
    }

    public Boolean getIsOn() {
        return isOn.get();
    }

    public void setIsOn(Boolean value) {
        this.isOn.set(value);
    }
}
