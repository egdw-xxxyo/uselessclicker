package org.dikhim.jclicker.configuration.newconfig.localization;

public class Languages {
    private Language english = new Language("en", "English", "English");
    private Language russian = new Language("ru", "Russian", "Русский");

    public Language english() {
        return english;
    }

    public Language russian() {
        return russian;
    }
}
