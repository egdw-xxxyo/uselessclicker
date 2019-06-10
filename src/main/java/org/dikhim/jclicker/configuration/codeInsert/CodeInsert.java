package org.dikhim.jclicker.configuration.codeInsert;

import javafx.beans.property.IntegerProperty;
import org.dikhim.jclicker.configuration.property.IntegerConfigProperty;
import org.dikhim.jclicker.configuration.property.SimpleConfigElement;

import java.util.prefs.Preferences;

public class CodeInsert extends SimpleConfigElement {
    private IntegerConfigProperty lineSize;

    public CodeInsert(String name, Preferences preferences) {
        super(name, preferences);
        lineSize = new IntegerConfigProperty("lineSize", 60, getPreferences());
    }

    @Override
    public void save() {
        lineSize.save();
    }

    @Override
    public void resetToDefault() {
        lineSize.resetToDefault();
    }

    @Override
    public void resetToSaved() {
        lineSize.resetToSaved();
    }

    public int getLineSize() {
        return lineSize.get();
    }

    public IntegerProperty lineSizeProperty() {
        return lineSize.getProperty();
    }

    public void setLineSize(int lineSize) {
        this.lineSize.set(lineSize);
    }
}
