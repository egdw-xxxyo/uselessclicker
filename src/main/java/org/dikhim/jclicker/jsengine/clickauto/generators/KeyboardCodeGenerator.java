package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;

public class KeyboardCodeGenerator extends SimpleCodeGenerator {
    public KeyboardCodeGenerator(int lineSize) {
        super("key", KeyboardObject.class, lineSize);
    }
    
    public KeyboardCodeGenerator() {
        super("key", KeyboardObject.class);
    }
}
