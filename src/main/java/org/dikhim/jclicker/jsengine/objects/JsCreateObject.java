package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.jsengine.objects.Classes.Image;

public class JsCreateObject implements CreateObject {
    @Override
    public Image image(int width, int height) {
        return new Image(width, height);
    }
}
