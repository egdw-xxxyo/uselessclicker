package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.CreateObject;

public class CreateCodeGenerator extends SimpleCodeGenerator {
    public CreateCodeGenerator(int lineSize) {
        super("create", CreateObject.class, lineSize);
    }
    
    public CreateCodeGenerator() {
        super("create", CreateObject.class);
    }
}
