package org.dikhim.jclicker.util.output;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StringPropertyOutput implements CustomOutput {
    private StringProperty stringProperty;

    public StringPropertyOutput(){
        this.stringProperty = new SimpleStringProperty("");
    }

    @Override
    public void print(String text) {
        if(stringProperty.get().length()>10000) {

            stringProperty.set(stringProperty.get().substring(500) + text);
        }else {
            stringProperty.set(stringProperty.get() + text);
        }
    }

    @Override
    public void println(String text) {
        if(stringProperty.get().length()>10000) {

            stringProperty.set(stringProperty.get().substring(500) + text + "\n");
        }else {
            stringProperty.set(stringProperty.get() + text + "\n");
        }
    }

    public StringProperty getProperty() {
        return stringProperty;
    }
}
