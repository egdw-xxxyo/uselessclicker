package org.dikhim.jclicker.configuration.newconfig.localization;

public class Language {
    private final String id;
    private final String englishName;
    private final String nativeName;
    
    public Language(String id, String englishName, String nativeName) {
        this.id = id;
        this.englishName = englishName;
        this.nativeName = nativeName;
    }

    public String id() {
        return id;
    }

    public String englishName() {
        return englishName;
    }

    public String nativeName() {
        return nativeName;
    }
}
