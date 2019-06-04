package org.dikhim.jclicker.configuration.newconfig;

import org.dikhim.jclicker.configuration.newconfig.hotkeys.HotKeys;

import java.util.prefs.Preferences;

public class Configuration extends SimpleConfigElement {
    private HotKeys hotKeys;

    public Configuration() {
        super("UselessClickerConfig", Preferences.userRoot());

        hotKeys = new HotKeys("hotKeys", getPreferences());
    }

    @Override
    public void save() {
        hotKeys.save();
    }

    @Override
    public void resetToDefault() {
        hotKeys.resetToDefault();
    }

    @Override
    public void resetToSaved() {
        hotKeys.resetToSaved();
    }

    public HotKeys hotKeys() {
        return hotKeys;
    }
}
