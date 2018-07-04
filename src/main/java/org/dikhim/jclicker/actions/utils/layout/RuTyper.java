package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

public class RuTyper extends SimpleTyper {
    public RuTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new RuLayout());
    }
}
