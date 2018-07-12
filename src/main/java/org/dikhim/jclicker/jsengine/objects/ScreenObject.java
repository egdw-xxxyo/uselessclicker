package org.dikhim.jclicker.jsengine.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ScreenObject {
    BufferedImage getImage(int x0, int y0, int x1, int y1);

    BufferedImage getImage(Point p1, Point p2);

    BufferedImage getImage(Rectangle rectangle);

    BufferedImage getFilledImage(int x0, int y0, int x1, int y1);
    
    BufferedImage getFilledImage(Point p1, Point p2);
    
    int getHeight();

    int getWidth();
}
