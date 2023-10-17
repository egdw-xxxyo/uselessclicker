package org.dikhim.clickauto.jsengine.objects;


import org.dikhim.clickauto.jsengine.objects.Classes.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface CreateObject {
    Image image(int width, int height);    
    
    Image image(String zipBase64String);

    Image image(BufferedImage bufferedImage);

    Image imageFile(String path);

    Point point(int x, int y);

    Rectangle rectangle(int x, int y, int width, int height);
    
    Rectangle rectangle(Point point, int width, int height);
    
    Rectangle rectangle(Point p1, Point p2);
}
