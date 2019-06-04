package org.dikhim.jclicker.configuration.newconfig;

public interface ConfigElement {
    void save();

    void resetToDefault();

    void resetToSaved();

    String getName();
}
