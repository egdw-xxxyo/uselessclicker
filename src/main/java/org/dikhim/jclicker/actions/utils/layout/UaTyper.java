package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

public class UaTyper extends SimpleTyper {
    public UaTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new UaLayout());
    }
}
