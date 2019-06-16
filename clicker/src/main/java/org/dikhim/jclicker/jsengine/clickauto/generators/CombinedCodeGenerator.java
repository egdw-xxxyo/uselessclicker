package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.CombinedObject;

public class CombinedCodeGenerator extends SimpleCodeGenerator {
    public CombinedCodeGenerator(int lineSize) {
        super("combined", CombinedObject.class, lineSize);
    }
    
     public CombinedCodeGenerator() {
        super("combined", CombinedObject.class);
    }
}
