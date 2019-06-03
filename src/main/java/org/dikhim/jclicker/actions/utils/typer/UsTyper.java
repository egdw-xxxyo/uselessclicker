package org.dikhim.jclicker.actions.utils.typer;

import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;

public class UsTyper extends SimpleTyper {
    public UsTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new UsLayout());
    }
}
