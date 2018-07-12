package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.ScreenObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenObjectCodeGenerator extends SimpleCodeGenerator implements ScreenObject {
    ScreenObjectCodeGenerator(int lineSize) {
        super("screen", lineSize, ScreenObject.class);
    }

    @Override
    public BufferedImage getImage(int x0, int y0, int x1, int y1) {
        buildStringForCurrentMethod(x0, y0, x1, y1);
        return null;
    }

    @Override
    public BufferedImage getImage(Point p1, Point p2) {
        buildStringForCurrentMethod();
        return null;
    }

    @Override
    public BufferedImage getImage(Rectangle rectangle) {
        buildStringForCurrentMethod();
        return null;
    }

    @Override
    public BufferedImage getFilledImage(int x0, int y0, int x1, int y1) {
        buildStringForCurrentMethod(x0, y0, x1, y1);
        return null;
    }

    @Override
    public BufferedImage getFilledImage(Point p1, Point p2) {
        buildStringForCurrentMethod();
        return null;
    }

    @Override
    public int getHeight() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getWidth() {
        buildStringForCurrentMethod();
        return 0;
    }
}
