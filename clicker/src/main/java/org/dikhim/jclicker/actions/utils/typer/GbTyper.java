package org.dikhim.jclicker.actions.utils.typer;

import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;

public class GbTyper extends SimpleTyper {
    public GbTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new GbLayout());
    }
}
