package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.MouseObject;

public class MouseObjectCodeGenerator extends SimpleCodeGenerator implements MouseObject {

    public MouseObjectCodeGenerator(int lineSize) {
        super("mouse", lineSize, MouseObject.class);
    }

    public MouseObjectCodeGenerator() {
        super("mouse", MouseObject.class);
    }

    @Override
    public void button(String button, String action) {
        buildStringForCurrentMethod(button, action);
    }

    @Override
    public void buttonAt(String button, String action, int x, int y) {
        buildStringForCurrentMethod(button, action, x, y);
    }

    @Override
    public void click(String button) {
        buildStringForCurrentMethod(button);
    }

    @Override
    public void clickAt(String button, int x, int y) {
        buildStringForCurrentMethod(button, x, y);
    }

    @Override
    public int getMinDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getMoveDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getMultipliedMoveDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getMultipliedPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getMultipliedReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getMultipliedWheelDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public float getMultiplier() {
        buildStringForCurrentMethod();
        return 0;
    }
    
    @Override
    public int getPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public float getSpeed() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getWheelDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getX() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getY() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public void move(int dx, int dy) {
        buildStringForCurrentMethod(dx, dy);
    }

    @Override
    public void moveAndButton(String button, String action, int dx, int dy) {
        buildStringForCurrentMethod(button, action, dx, dy);
    }

    @Override
    public void moveAndClick(String button, int dx, int dy) {
        buildStringForCurrentMethod(button, dx, dy);
    }

    @Override
    public void moveAndPress(String button, int dx, int dy) {
        buildStringForCurrentMethod(button, dx, dy);
    }

    @Override
    public void moveAndRelease(String button, int dx, int dy) {
        buildStringForCurrentMethod(button, dx, dy);
    }

    @Override
    public void moveAndWheel(String direction, int amount, int dx, int dy) {
        buildStringForCurrentMethod(direction, amount, dx, dy);
    }

    @Override
    public void moveTo(int x, int y) {
        buildStringForCurrentMethod(x, y);
    }

    @Override
    public void press(String button) {
        buildStringForCurrentMethod(button);
    }

    @Override
    public void pressAt(String button, int x, int y) {
        buildStringForCurrentMethod(button, x, y);
    }

    @Override
    public void release(String button) {
        buildStringForCurrentMethod(button);
    }

    @Override
    public void releaseAt(String button, int x, int y) {
        buildStringForCurrentMethod(button, x, y);
    }

    @Override
    public void resetDelays() {
        buildStringForCurrentMethod();
    }

    @Override
    public void resetMultiplier() {
        buildStringForCurrentMethod();
    }

    @Override
    public void resetSpeed() {
        buildStringForCurrentMethod();
    }

    @Override
    public void setDelays(int delay) {
        buildStringForCurrentMethod(delay);
    }

    @Override
    public void setMinDelay(int minDelay) {
        buildStringForCurrentMethod(minDelay);
    }

    @Override
    public void setMoveDelay(int moveDelay) {
        buildStringForCurrentMethod(moveDelay);
    }

    @Override
    public void setMultiplier(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void setPressDelay(int pressDelay) {
        buildStringForCurrentMethod(pressDelay);
    }

    @Override
    public void setReleaseDelay(int releaseDelay) {
        buildStringForCurrentMethod(releaseDelay);
    }

    @Override
    public void setSpeed(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void setWheelDelay(int wheelDelay) {
        buildStringForCurrentMethod(wheelDelay);
    }

    @Override
    public void setX(int x) {
        buildStringForCurrentMethod(x);
    }

    @Override
    public void setY(int y) {
        buildStringForCurrentMethod(y);
    }

    @Override
    public void wheel(String direction, int amount) {
        buildStringForCurrentMethod(direction, amount);
    }

    @Override
    public void wheelAt(String direction, int amount, int x, int y) {
        buildStringForCurrentMethod(direction, amount, x, y);
    }
}
