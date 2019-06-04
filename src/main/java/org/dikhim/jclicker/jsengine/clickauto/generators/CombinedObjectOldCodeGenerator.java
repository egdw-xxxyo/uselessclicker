package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.CombinedObject;

public class CombinedObjectOldCodeGenerator extends SimpleOldCodeGenerator implements CombinedObject {
    public CombinedObjectOldCodeGenerator(int lineSize) {
        super("combined", lineSize, CombinedObject.class);
    }

    public CombinedObjectOldCodeGenerator() {
        super("combined", CombinedObject.class);
    }

    @Override
    public void run(String encoding, String code) {
        buildStringForCurrentMethod(encoding, code);
    }
}
