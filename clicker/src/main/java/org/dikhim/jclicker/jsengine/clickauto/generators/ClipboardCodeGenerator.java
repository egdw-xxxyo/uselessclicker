package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.ClipboardObject;

public class ClipboardCodeGenerator extends SimpleCodeGenerator {
    public ClipboardCodeGenerator(int lineSize) {
        super("clipboard", ClipboardObject.class, lineSize);
    }
    
    public ClipboardCodeGenerator() {
        super("clipboard", ClipboardObject.class);
    }
}
