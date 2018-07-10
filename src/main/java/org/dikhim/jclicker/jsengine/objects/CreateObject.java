package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.jsengine.objects.Classes.Image;

import java.awt.*;

public interface CreateObject {
    Image image(int width, int height);

    Image image(String zipBase64String);

    Point point(int x, int y);
}
