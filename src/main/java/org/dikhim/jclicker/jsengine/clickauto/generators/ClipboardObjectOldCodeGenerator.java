package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.ClipboardObject;

public class ClipboardObjectOldCodeGenerator extends SimpleOldCodeGenerator implements ClipboardObject {

    public ClipboardObjectOldCodeGenerator(int lineSize) {
        super("clipboard", lineSize, ClipboardObject.class);
    }

    public ClipboardObjectOldCodeGenerator() {
        super("clipboard", ClipboardObject.class);
    }

    @Override
    public String get() {
        buildStringForCurrentMethod();
        return null;
    }

    @Override
    public void set(String str) {
        buildStringForCurrentMethod(str);
    }
}
