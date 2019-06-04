package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.ScreenObject;

public class ScreenCodeGenerator extends SimpleCodeGenerator {
    public ScreenCodeGenerator(int lineSize) {
        super("screen", ScreenObject.class, lineSize);
    }
    
    public ScreenCodeGenerator() {
        super("screen", ScreenObject.class);
    }
}
