package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.CombinedObject;

public class CombinedObjectCodeGenerator extends SimpleCodeGenerator implements CombinedObject{
    public CombinedObjectCodeGenerator(int lineSize) {
        super("combined", lineSize);
    }


    @Override
    public void run(String code) {
        buildStringForCurrentMethod(code);
    }
}
