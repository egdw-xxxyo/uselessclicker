package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

public interface Layout {
    void type(KeyboardObject keyboardObject, String text);
    
    String getLyout();
    
    String getDescription();
}
