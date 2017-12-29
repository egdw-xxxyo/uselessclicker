package org.dikhim.jclicker.util.output;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SystemOutput implements CustomOutput{
    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public StringProperty getStringProperty() {
        return new SimpleStringProperty();
    }

    @Override
    public void clear() {

    }
}
