package org.dikhim.jclicker.configuration.newconfig.property;

import javafx.beans.property.Property;
import org.dikhim.jclicker.configuration.newconfig.ConfigElement;

public interface ConfigProperty<T> extends ConfigElement {
    T get();

    void set(T value);
    
    Property getProperty();
}
