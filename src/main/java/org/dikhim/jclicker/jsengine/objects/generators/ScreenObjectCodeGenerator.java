package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.jsengine.objects.ScreenObject;

import java.awt.*;


public class ScreenObjectCodeGenerator extends SimpleCodeGenerator implements ScreenObject {
    public ScreenObjectCodeGenerator(int lineSize) {
        super("screen", lineSize, ScreenObject.class);
    }

    @Override
    public Image getImage(int x0, int y0, int x1, int y1) {
        buildStringForCurrentMethod(x0, y0, x1, y1);
        return null;
    }

    @Override
    public Image getImage(Point p1, Point p2) {
        buildStringForCurrentMethod();
        return null;
    }

    @Override
    public Image getImage(Rectangle rectangle) {
        buildStringForCurrentMethod();
        return null;
    }

    @Override
    public Image getFilledImage(int x0, int y0, int x1, int y1) {
        buildStringForCurrentMethod(x0, y0, x1, y1);
        return null;
    }

    @Override
    public Image getFilledImage(Point p1, Point p2) {
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
