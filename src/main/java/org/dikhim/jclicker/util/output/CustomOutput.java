package org.dikhim.jclicker.util.output;

import javafx.beans.property.StringProperty;

public interface CustomOutput {
    public void print(String text);

    public void println(String text);

    public StringProperty getStringProperty();

    public void clear();
}
