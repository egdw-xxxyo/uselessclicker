package org.dikhim.jclicker.jsengine.clickauto.objects;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.clickauto.jsengine.objects.ScriptCreateObject;

public class UselessCreateObject extends ScriptCreateObject implements CreateObject {
    @Override
    public Image image(int width, int height) {
        return super.image(width, height);
    }

    @Override
    public Image image(String zipBase64String) {
        return super.image(zipBase64String);
    }

    @Override
    public Image imageFile(String path) {
        return super.imageFile(path);
    }
}
