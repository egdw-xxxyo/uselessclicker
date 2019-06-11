package org.dikhim.jclicker.configuration.property;

public interface ConfigElement {
    void save();

    void resetToDefault();

    void resetToSaved();

    String getName();
}
