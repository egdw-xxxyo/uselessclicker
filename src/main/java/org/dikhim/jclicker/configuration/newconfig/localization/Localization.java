package org.dikhim.jclicker.configuration.newconfig.localization;

import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.configuration.newconfig.property.SimpleConfigElement;
import org.dikhim.jclicker.configuration.newconfig.property.StringConfigProperty;

import java.util.prefs.Preferences;

public class Localization extends SimpleConfigElement {
    private final Languages languages = new Languages();

    private final StringConfigProperty applicationLanguageId;

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

    public Languages languages() {
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
