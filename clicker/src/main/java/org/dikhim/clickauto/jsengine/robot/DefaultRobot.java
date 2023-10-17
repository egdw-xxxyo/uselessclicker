package org.dikhim.clickauto.jsengine.robot;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DefaultRobot implements Robot {
    private java.awt.Robot robot;

    public DefaultRobot(java.awt.Robot robot) {
        this.robot = robot;
    }

    @Override
    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    @Override
    public void mousePress(int buttons) {
        robot.mousePress(buttons);
    }

    @Override
    public void mouseRelease(int buttons) {
        robot.mouseRelease(buttons);
    }

    @Override
    public void mouseWheel(int wheelAmt) {
        robot.mouseWheel(wheelAmt);
    }

    @Override
    public void keyPress(int keycode) {
        robot.keyPress(keycode);
    }

    @Override
    public void keyRelease(int keycode) {
        robot.keyRelease(keycode);
    }

    @Override
    public Color getPixelColor(int x, int y) {
        return robot.getPixelColor(x, y);
    }

    @Override
    public BufferedImage createScreenCapture(Rectangle screenRect) {
        return robot.createScreenCapture(screenRect);
    }

    @Override
    public boolean isAutoWaitForIdle() {
        return robot.isAutoWaitForIdle();
    }

    @Override
    public void setAutoWaitForIdle(boolean isOn) {
        robot.setAutoWaitForIdle(isOn);
    }

    @Override
    public int getAutoDelay() {
        return robot.getAutoDelay();
    }

    @Override
    public void setAutoDelay(int ms) {
        robot.setAutoDelay(ms);
    }

    @Override
    public void delay(int ms) {
        robot.delay(ms);
    }

    @Override
    public void waitForIdle() {
        robot.waitForIdle();
    }
}
