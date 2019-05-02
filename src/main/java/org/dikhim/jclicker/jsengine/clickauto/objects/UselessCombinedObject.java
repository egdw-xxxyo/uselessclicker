package org.dikhim.jclicker.jsengine.clickauto.objects;

import org.dikhim.clickauto.jsengine.objects.KeyboardObject;
import org.dikhim.clickauto.jsengine.objects.MouseObject;
import org.dikhim.clickauto.jsengine.objects.ScriptCombinedObject;
import org.dikhim.clickauto.jsengine.objects.SystemObject;

public class UselessCombinedObject extends ScriptCombinedObject implements CombinedObject {
    public UselessCombinedObject(MouseObject mouseObject, KeyboardObject keyboardObject, SystemObject systemObject) {
        super(mouseObject, keyboardObject, systemObject);
    }
}
