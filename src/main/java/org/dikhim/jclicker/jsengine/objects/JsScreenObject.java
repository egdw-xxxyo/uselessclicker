package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.jsengine.robot.Robot;
import org.dikhim.jclicker.jsengine.robot.RobotStatic;

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
    public BufferedImage getImage(Rectangle rectangle) {
        synchronized (monitor) {
            BufferedImage bufferedImage = RobotStatic.get().createScreenCapture(rectangle);

            return bufferedImage;
        }
    }

    @Override
    public BufferedImage getImage(int x0, int y0, int x1, int y1) {
        synchronized (monitor) {
            Rectangle rectangle = createRectangle(x0, y0, x1, y1);
            return RobotStatic.get().createScreenCapture(rectangle);
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
        if (x0 < x1 && y0 < y1 && x0 >= 0 && y0 >= 0 && x1 <= getWidth() && y1 <= getHeight())
            return new Rectangle(x0, y0, x1 - x0, y1 - y0);

        int min = Math.min(x0, x1);
        int max = Math.max(x0, x1);
        x0 = Math.max(min, 0);
        x1 = Math.min(max, getWidth());

        min = Math.min(y0, y1);
        max = Math.max(y0, y1);
        y0 = Math.max(min, 0);
        y1 = Math.min(max, getHeight());

        return new Rectangle(x0, y0, x1 - x0, y1 - y0);
    }
}
