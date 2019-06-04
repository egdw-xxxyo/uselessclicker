package org.dikhim.jclicker.configuration.newconfig.property;

import org.junit.Before;
import org.junit.Test;

import java.util.prefs.Preferences;

import static org.junit.Assert.*;

public class StringConfigPropertyTest {
    ConfigProperty<String> property;
    @Before
    public void init() {
        Preferences preferences = Preferences.userRoot().node("testing");
        property = new StringConfigProperty("testProperty", "default", preferences);
        property.resetToDefault();
        property.save();
    }
    @Test
    public void save() {
        assertEquals("default", property.get());
        property.set("changed");
        property.save();
        property.resetToSaved();
        assertEquals("changed", property.get());

    }

    @Test
    public void resetToDefault() {
        assertEquals("default", property.get());
        property.set("changed");
        property.resetToDefault();
        assertEquals("default", property.get());
    }

    @Test
    public void resetToSaved() {
        assertEquals("default", property.get());
        property.set("changed");
        property.save();
        property.set("changed2");
        property.resetToSaved();
        assertEquals("changed", property.get());
    }
    
    @Test
    public void getAndSet() {
        assertEquals("default", property.get());
        property.set("changed");
        assertEquals("changed", property.get());
    }
    
}