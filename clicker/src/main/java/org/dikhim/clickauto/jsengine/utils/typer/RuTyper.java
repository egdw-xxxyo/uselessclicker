package org.dikhim.clickauto.jsengine.utils.typer;

import org.dikhim.clickauto.jsengine.objects.KeyboardObject;

public class RuTyper extends SimpleTyper {
    public RuTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new RuLayout());
    }
}
