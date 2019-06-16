package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.MouseObject;

public class MouseCodeGenerator extends SimpleCodeGenerator {
    public MouseCodeGenerator(int lineSize) {
        super("mouse", MouseObject.class, lineSize);
    }
    
    public MouseCodeGenerator() {
        super("mouse", MouseObject.class);
    }
}
