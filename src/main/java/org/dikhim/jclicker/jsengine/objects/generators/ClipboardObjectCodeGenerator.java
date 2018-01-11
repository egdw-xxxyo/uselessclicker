package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.ClipboardObject;

public class ClipboardObjectCodeGenerator extends SimpleCodeGenerator implements ClipboardObject {
    public ClipboardObjectCodeGenerator(String objectName, int lineSize) {
        super(objectName, lineSize);
    }

    public ClipboardObjectCodeGenerator(int lineSize) {
        super("clipboard", lineSize);
    }

    public String get() {
        buildStringForCurrentMethod();
        return null;
    }

    public void set(String str) {
        buildStringForCurrentMethod(str);
    }
}
