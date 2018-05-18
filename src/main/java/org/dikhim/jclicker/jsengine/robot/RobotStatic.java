package org.dikhim.jclicker.jsengine.robot;

import org.dikhim.jclicker.util.Out;

public class RobotStatic {
    private static final Robot robot;
    private static final Object monitor = new Object();

    static {
        Robot robotTemp;
        try {
            java.awt.Robot awtRobot = new java.awt.Robot();
            robotTemp = new DefaultRobot(awtRobot, monitor);
        } catch (Exception e) {
            Out.println("Cannot create 'Robot' object. Cause:" + e.getMessage());
            Out.println("Keyboard and mouse control will be unavailable");
            robotTemp = new EmptyRobot(monitor);
        }
        robot = robotTemp;
    }

    public static Robot get() {
        return robot;
    }

}
