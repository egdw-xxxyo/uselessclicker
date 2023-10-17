package org.dikhim.clickauto.jsengine.robot;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EmptyRobot implements Robot {

    public EmptyRobot( ) {
    }

    @Override
    public void mouseMove(int x, int y) {

    }

    @Override
    public void mousePress(int buttons) {

    }

    @Override
    public void mouseRelease(int buttons) {

    }

    @Override
    public void mouseWheel(int wheelAmt) {

    }

    @Override
    public void keyPress(int keycode) {

    }

    @Override
    public void keyRelease(int keycode) {

    }

    @Override
    public Color getPixelColor(int x, int y) {
        return new Color(0, 0, 0);
    }

    @Override
    public BufferedImage createScreenCapture(Rectangle screenRect) {
        return new BufferedImage(0, 0, BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public boolean isAutoWaitForIdle() {
        return false;
    }

    @Override
    public void setAutoWaitForIdle(boolean isOn) {

    }

    @Override
    public int getAutoDelay() {
        return 0;
    }

    @Override
    public void setAutoDelay(int ms) {

    }

    @Override
    public void delay(int ms) {

    }

    @Override
    public void waitForIdle() {

    }
}
