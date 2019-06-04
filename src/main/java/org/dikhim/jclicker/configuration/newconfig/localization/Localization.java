package org.dikhim.jclicker.configuration.newconfig.localization;

import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.configuration.newconfig.SimpleConfigElement;
import org.dikhim.jclicker.configuration.newconfig.property.StringConfigProperty;

import java.util.prefs.Preferences;

public class Localization extends SimpleConfigElement {
    private Languages languages = new Languages();

    private StringConfigProperty applicationLanguageId;

    public Localization(String name, Preferences preferences) {
        super(name, preferences);

        this.applicationLanguageId = new StringConfigProperty("appLangId", "en", getPreferences());
    }

    @Override
    public void save() {
        applicationLanguageId.save();
    }

    @Override
    public void resetToDefault() {
        applicationLanguageId.resetToDefault();
    }

    @Override
    public void resetToSaved() {
        applicationLanguageId.resetToSaved();
    }

    public Languages getLanguages() {
        return languages;
    }

    public String getApplicationLanguageId() {
        return applicationLanguageId.get();
    }
    
    public StringProperty applicationLanguageIdProperty() {
        return applicationLanguageId.getProperty();
    }

    public void setApplicationLanguageId(String id) {
        applicationLanguageId.set(id);
    }
}
