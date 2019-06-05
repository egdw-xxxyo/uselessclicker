package org.dikhim.jclicker.configuration.localization;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Languages {
    private final Language english = new Language("en", "English", "English");
    private final Language russian = new Language("ru", "Russian", "Русский");

    public List<Language> list;
    {
        Language[] arr = {english, russian};
        list = Collections.unmodifiableList(Arrays.asList(arr));
    }

    public Language english() {
        return english;
    }
    
    public Language russian() {
        return russian;
    }
    
    public List<Language> list() {
        return list;
    }
}
