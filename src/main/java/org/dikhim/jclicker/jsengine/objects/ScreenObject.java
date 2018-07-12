package org.dikhim.jclicker.jsengine.objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ScreenObject {
    BufferedImage getImage(Rectangle rectangle);

    BufferedImage getImage(int x0, int y0, int x1, int y1);

    BufferedImage getFilledImage(int x0, int y0, int x1, int y1);

    int getHeight();

    int getWidth();
}
