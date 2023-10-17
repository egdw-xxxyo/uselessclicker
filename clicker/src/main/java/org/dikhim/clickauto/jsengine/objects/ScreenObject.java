package org.dikhim.clickauto.jsengine.objects;


import org.dikhim.clickauto.jsengine.objects.Classes.Image;

import java.awt.*;

public interface ScreenObject {
    Image getImage(int x0, int y0, int x1, int y1);

    Image getImage(Point p1, Point p2);

    Image getImage(Rectangle rectangle);

    Image getFilledImage(int x0, int y0, int x1, int y1);
    
    Image getFilledImage(Point p1, Point p2);
    
    int getHeight();

    int getWidth();
}
