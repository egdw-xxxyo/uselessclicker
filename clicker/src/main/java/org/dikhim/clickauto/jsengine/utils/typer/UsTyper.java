package org.dikhim.clickauto.jsengine.utils.typer;


import org.dikhim.clickauto.jsengine.objects.KeyboardObject;

public class UsTyper extends SimpleTyper {
    public UsTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new UsLayout());
    }
}
