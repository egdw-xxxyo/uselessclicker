package org.dikhim.jclicker.actions.utils.typer;

import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;

public class RuTyper extends SimpleTyper {
    public RuTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new RuLayout());
    }
}
