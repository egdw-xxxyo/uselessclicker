package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.MouseObject;

public class MouseObjectCodeGenerator extends SimpleCodeGenerator implements MouseObject {

    public MouseObjectCodeGenerator(int lineSize) {
        super("mouse", lineSize);
    }

    public void button(String button, String action) {
        buildStringForCurrentMethod(button, action);
    }

    public void buttonAt(String button, String action, int x, int y) {
        buildStringForCurrentMethod(button, action, x, y);
    }

    public void click(String button) {
        buildStringForCurrentMethod(button);
    }

    public void clickAt(String button, int x, int y) {
        buildStringForCurrentMethod(button, x, y) ;
    }

    public int getMinDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getMoveDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getMultipliedMoveDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getMultipliedPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getMultipliedReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getMultipliedWheelDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public float getMultiplier() {
        buildStringForCurrentMethod();
        return 0;
    }


    public int getPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public float getSpeed() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getWheelDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getX() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getY() {
        buildStringForCurrentMethod();
        return 0;
    }

    public void move(int dx, int dy) {
        buildStringForCurrentMethod(dx, dy);
    }

    public void moveAbsolute(String path) {
        buildStringForCurrentMethod(path);
    }

    public void moveAbsolute_D(String path) {
        buildStringForCurrentMethod(path);
    }

    public void moveAndButton(String button, String action, int dx, int dy) {
        buildStringForCurrentMethod(button, action, dx, dy);
    }

    public void moveAndClick(String button, int dx, int dy) {
        buildStringForCurrentMethod(button, dx, dy);
    }

    public void moveAndPress(String button, int dx, int dy) {
        buildStringForCurrentMethod(button, dx, dy);
    }

    public void moveAndRelease(String button, int dx, int dy) {
        buildStringForCurrentMethod(button, dx, dy);
    }

    public void moveAndWheel(String direction, int amount, int dx, int dy) {
        buildStringForCurrentMethod(direction, amount, dx, dy);
    }

    public void moveRelative(String path) {
        buildStringForCurrentMethod(path);
    }

    public void moveRelative_D(String path) {
        buildStringForCurrentMethod(path);
    }

    public void moveTo(int x, int y) {
        buildStringForCurrentMethod(x, y);
    }

    public void press(String button) {
        buildStringForCurrentMethod(button);
    }

    public void pressAt(String button, int x, int y) {
        buildStringForCurrentMethod(button, x, y);
    }

    public void release(String button) {
        buildStringForCurrentMethod(button);
    }

    public void releaseAt(String button, int x, int y) {
        buildStringForCurrentMethod(button, x, y);
    }

    public void resetDelays() {
        buildStringForCurrentMethod();
    }

    public void resetMultiplier() {
        buildStringForCurrentMethod();
    }

    public void resetSpeed() {
        buildStringForCurrentMethod();
    }

    public void setDelays(int delay) {
        buildStringForCurrentMethod(delay);
    }

    public void setMinDelay(int minDelay) {
        buildStringForCurrentMethod(minDelay);
    }

    public void setMoveDelay(int moveDelay) {
        buildStringForCurrentMethod(moveDelay);
    }

    public void setMultiplier(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    public void setPressDelay(int pressDelay) {
        buildStringForCurrentMethod(pressDelay);
    }

    public void setReleaseDelay(int releaseDelay) {
        buildStringForCurrentMethod(releaseDelay);
    }

    public void setSpeed(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    public void setWheelDelay(int wheelDelay) {
        buildStringForCurrentMethod(wheelDelay);
    }

    public void setX(int x) {
        buildStringForCurrentMethod(x);
    }

    public void setY(int y) {
        buildStringForCurrentMethod(y);
    }

    public void wheel(String direction, int amount) {
        buildStringForCurrentMethod(direction, amount);
    }

    public void wheelAt(String direction, int amount, int x, int y) {
        buildStringForCurrentMethod(direction, amount, x, y);
    }


}
