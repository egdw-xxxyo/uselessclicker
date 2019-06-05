package org.dikhim.jclicker.configuration.newconfig.property;

public interface ConfigElement {
    void save();

    void resetToDefault();

    void resetToSaved();

    String getName();
}
