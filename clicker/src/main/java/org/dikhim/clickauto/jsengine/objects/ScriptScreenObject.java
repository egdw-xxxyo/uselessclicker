package org.dikhim.clickauto.jsengine.objects;


import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.util.ShapeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScriptScreenObject implements ScreenObject {
    protected final Robot robot;

    public ScriptScreenObject(Robot robot) {
        this.robot = robot;
    }

    @Override
    public Image getImage(Rectangle rectangle) {
        synchronized (robot) {
            return new Image(robot.createScreenCapture(rectangle));
        }
    }

    @Override
    public Image getImage(int x0, int y0, int x1, int y1) {
        synchronized (robot) {
            return getImage(createFitsRectangle(x0, y0, x1, y1));
        }
    }

    @Override
    public Image getImage(Point p1, Point p2) {
        synchronized (robot) {
            return getImage(p1.x, p1.y, p2.x, p2.y);
        }
    }

    @Override
    public Image getFilledImage(int x0, int y0, int x1, int y1) {
        synchronized (robot) {
            Rectangle rectangle = ShapeUtil.createRectangle(x0, y0, x1, y1);
            Rectangle fitsRectangle = createFitsRectangle(x0, y0, x1, y1);

            BufferedImage capturedImage = robot.createScreenCapture(fitsRectangle);
            if (rectangle.equals(fitsRectangle)) return new Image(capturedImage);
            BufferedImage outputImage = new BufferedImage(rectangle.width, rectangle.height, capturedImage.getType());
            Graphics2D g = outputImage.createGraphics();
            int dx = 0;
            int dy = 0;
            if (rectangle.x < fitsRectangle.x) {
                dx = fitsRectangle.x - rectangle.x;
            }
            if (rectangle.y < fitsRectangle.y) {
                dy = fitsRectangle.y - rectangle.y;
            }

            g.drawImage(capturedImage, dx, dy, fitsRectangle.width, fitsRectangle.height, null);
            g.dispose();
            return new Image(outputImage);
        }
    }

    @Override
    public Image getFilledImage(Point p1, Point p2) {
        synchronized (robot) {
            return getFilledImage(p1.x, p1.y, p2.x, p2.y);
        }
    }

    @Override
    public int getHeight() {
        return Toolkit.getDefaultToolkit().getScreenSize().height;
    }

    @Override
    public int getWidth() {
        return Toolkit.getDefaultToolkit().getScreenSize().width;
    }

    protected Rectangle createFitsRectangle(int x0, int y0, int x1, int y1) {
        Rectangle parent = new Rectangle(0, 0, getWidth(), getHeight());
        Rectangle child = ShapeUtil.createRectangle(x0, y0, x1, y1);
        return ShapeUtil.createMinimalRectangleFitIntoParent(parent, child);
    }

    
}
