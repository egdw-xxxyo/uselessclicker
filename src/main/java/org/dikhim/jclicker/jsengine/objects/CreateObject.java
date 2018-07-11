package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.jsengine.objects.Classes.Image;

import java.awt.*;

public interface CreateObject {

    Image image(String zipBase64String);

    Image imageFile(String path);

    Point point(int x, int y);
}
