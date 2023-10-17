package org.dikhim.clickauto.jsengine.utils.typer;


import org.dikhim.clickauto.jsengine.objects.KeyboardObject;

public class GbTyper extends SimpleTyper {
    public GbTyper(KeyboardObject keyboardObject) {
        super(keyboardObject, new GbLayout());
    }
}
