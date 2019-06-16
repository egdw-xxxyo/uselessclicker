package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.SystemObject;

public class SystemCodeGenerator extends SimpleCodeGenerator {
    public SystemCodeGenerator(int lineSize) {
        super("system", SystemObject.class, lineSize);
    }
    
    public SystemCodeGenerator() {
        super("system", SystemObject.class);
    }
}
