package org.dikhim.jclicker.configuration.property;

import javafx.beans.property.Property;

public interface ConfigProperty<T> extends ConfigElement {
    T get();

    void set(T value);
    
    Property getProperty();
}
