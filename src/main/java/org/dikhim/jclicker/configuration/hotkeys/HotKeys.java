package org.dikhim.jclicker.configuration.hotkeys;


import org.dikhim.jclicker.configuration.property.SimpleConfigElement;

import java.util.prefs.Preferences;

public class HotKeys extends SimpleConfigElement {
    private final Shortcut runScript;
    private final Shortcut stopScript;
    private final Shortcut combinedControl;
    private final Shortcut mouseControl;

    public HotKeys(String name, Preferences preferences) {
        super(name, preferences);
        
        runScript = new Shortcut(
                "runScript", "CONTROL ALT A", "execution", getPreferences());
        
        stopScript = new Shortcut(
                "stopScript", "CONTROL ALT S", "execution", getPreferences());
        
        combinedControl = new Shortcut(
                "combinedControl", "ALT", "recording", getPreferences());
        
        mouseControl = new Shortcut(
                "mouseControl", "CONTROL", "recording", getPreferences());
    }


    @Override
    public void save() {
        runScript.save();
        stopScript.save();
        combinedControl.save();
        mouseControl.save();
    }

    @Override
    public void resetToDefault() {
        runScript.resetToDefault();
        stopScript.resetToDefault();
        combinedControl.resetToDefault();
        mouseControl.resetToDefault();
    }

    @Override
    public void resetToSaved() {
        runScript.resetToSaved();
        stopScript.resetToSaved();
        combinedControl.resetToSaved();
        mouseControl.resetToSaved();
    }

    public Shortcut runScript() {
        return runScript;
    }

    public Shortcut stopScript() {
        return stopScript;
    }

    public Shortcut combinedControl() {
        return combinedControl;
    }

    public Shortcut mouseControl() {
        return mouseControl;
    }
}
