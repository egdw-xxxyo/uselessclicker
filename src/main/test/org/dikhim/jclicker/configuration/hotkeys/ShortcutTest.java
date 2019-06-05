package org.dikhim.jclicker.configuration.hotkeys;

import org.junit.Test;

import java.util.prefs.Preferences;

import static org.junit.Assert.assertEquals;

public class ShortcutTest {

    @Test
    public void complexTest() {
        Preferences preferences = Preferences.userRoot().node("testing");

        Shortcut shortcut = new Shortcut("test", "default", "category", preferences);
        
        assertEquals("default", shortcut.getKeys());
        shortcut.setKeys("changed");
        assertEquals("changed", shortcut.getKeys());
        
        shortcut.resetToDefault();
        assertEquals("default", shortcut.getKeys());

        shortcut.setKeys("changed");
        shortcut.save();
        shortcut.setKeys("changed2");
        shortcut.resetToDefault();
        shortcut.resetToSaved();
        assertEquals("changed", shortcut.getKeys());
    }
}