package org.dikhim.jclicker.configuration.localization;

import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.configuration.property.SimpleConfigElement;
import org.dikhim.jclicker.configuration.property.StringConfigProperty;

import java.util.Locale;
import java.util.prefs.Preferences;

public class Localization extends SimpleConfigElement {
    private final Languages languages = new Languages();

    private final StringConfigProperty applicationLanguageId;

    private Locale locale;

    public Localization(String name, Preferences preferences) {
        super(name, preferences);

        this.applicationLanguageId = new StringConfigProperty("appLangId", "en", getPreferences());
        this.locale = new Locale(applicationLanguageId.get());
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

    public Locale getLocale() {
        return locale;
    }
}
