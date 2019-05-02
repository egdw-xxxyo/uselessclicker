package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.jsengine.clickauto.objects.ScreenObject;
import org.dikhim.jclicker.jsengine.robot.Robot;
import org.dikhim.jclicker.util.ShapeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;


public class JsScreenObject implements ScreenObject {

    private org.dikhim.jclicker.jsengine.robot.Robot robot;
    private final Object monitor;

    public JsScreenObject(Robot robot) {
        this.robot = robot;
        this.monitor = robot.getMonitor();
    }

    @Override
    public Image getImage(int x0, int y0, int x1, int y1) {
        synchronized (monitor) {
            Rectangle rectangle = createFitsRectangle(x0, y0, x1, y1);
            return getImage(rectangle);
        }
    }

    @Override
    public Image getImage(Point p1, Point p2) {
        synchronized (monitor) {
            return getImage(p1.x, p1.y, p2.x, p2.y);
        }
    }
    
    @Override
    public Image getImage(Rectangle rectangle) {
        synchronized (monitor) {
            return new Image(robot.createScreenCapture(rectangle));
        }
    }

    @Override
    public Image getFilledImage(int x0, int y0, int x1, int y1) {
        synchronized (monitor) {
            Rectangle rectangle = createRectangle(x0, y0, x1, y1);
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
        synchronized (monitor) {
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

    private Rectangle createRectangle(int x0, int y0, int x1, int y1) {
        return ShapeUtil.createRectangle(x0, y0, x1, y1);
    }

    private Rectangle createFitsRectangle(int x0, int y0, int x1, int y1) {
        if (x0 < x1 && y0 < y1 && x0 >= 0 && y0 >= 0 && x1 <= getWidth() && y1 <= getHeight())
            return ShapeUtil.createRectangle(x0, y0, x1, y1);

        
        
        int min = Math.min(x0, x1);
        int max = Math.max(x0, x1);
        x0 = Math.max(min, 0);
        x1 = Math.min(max, getWidth());

        min = Math.min(y0, y1);
        max = Math.max(y0, y1);
        y0 = Math.max(min, 0);
        y1 = Math.min(max, getHeight());

        return ShapeUtil.createRectangle(x0, y0, x1, y1);
    }
}
