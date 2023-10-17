package org.dikhim.clickauto.jsengine.robot;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface Robot {

    public void mouseMove(int x, int y);

    void mousePress(int buttons);

    void mouseRelease(int buttons);

    void mouseWheel(int wheelAmt);

    void keyPress(int keycode);

    void keyRelease(int keycode);

    Color getPixelColor(int x, int y);

    BufferedImage createScreenCapture(Rectangle screenRect);

    boolean isAutoWaitForIdle();

    void setAutoWaitForIdle(boolean isOn);

    int getAutoDelay();

    void setAutoDelay(int ms);

    void delay(int ms);

    void waitForIdle();

    String toString();
}
