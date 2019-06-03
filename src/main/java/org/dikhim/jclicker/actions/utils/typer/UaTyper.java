package org.dikhim.jclicker.actions.utils.typer;

import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;

public class UaTyper extends SimpleTyper {
    public UaTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new UaLayout());
    }
}
