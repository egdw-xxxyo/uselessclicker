package org.dikhim.jclicker.configuration.newconfig.property;

import javafx.beans.property.Property;

public interface ConfigProperty<T> extends ConfigElement {
    T get();

    void set(T value);
    
    Property getProperty();
}
