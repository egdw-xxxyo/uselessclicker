package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.jsengine.objects.Classes.Image;

import java.awt.*;

public class JsCreateObject implements CreateObject {
    @Override
    public Image image(int width, int height) {
        return new Image(width, height);
    }

    @Override
    public Point point(int x, int y) {
        return new Point(x, y);
    }
}
