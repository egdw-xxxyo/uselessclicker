package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.ClipboardObject;

public class ClipboardObjectCodeGenerator extends SimpleCodeGenerator implements ClipboardObject {

    public ClipboardObjectCodeGenerator(int lineSize) {
        super("clipboard", lineSize, ClipboardObject.class);
    }

    public ClipboardObjectCodeGenerator() {
        super("clipboard", ClipboardObject.class);
    }


    public String get() {
        buildStringForCurrentMethod();
        return null;
    }

    public void set(String str) {
        buildStringForCurrentMethod(str);
    }
}
