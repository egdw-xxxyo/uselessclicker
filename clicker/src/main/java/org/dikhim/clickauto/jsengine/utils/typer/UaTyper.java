package org.dikhim.clickauto.jsengine.utils.typer;

import org.dikhim.clickauto.jsengine.objects.KeyboardObject;

public class UaTyper extends SimpleTyper {
    public UaTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new UaLayout());
    }
}
