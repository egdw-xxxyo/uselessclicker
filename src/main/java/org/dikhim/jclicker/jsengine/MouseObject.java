package org.dikhim.jclicker.jsengine;

import java.awt.Robot;

import org.dikhim.jclicker.events.MouseCodes;
import org.dikhim.jclicker.events.MouseEventsManager;
import org.dikhim.jclicker.util.MouseMoveUtil;
import org.dikhim.jclicker.util.output.Out;

import javax.script.ScriptException;

/**
 *
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class MouseObject {

    // Constants
    private final int PRESS_DELAY = 10;
    private final int RELEASE_DELAY = 10;
    private final int MOVE_DELAY = 10;
    private final int WHEEL_DELAY = 10;
    private final float MULTIPLIER = 1;

    private int pressDelay = PRESS_DELAY;
    private int releaseDelay = RELEASE_DELAY;
    private int moveDelay = MOVE_DELAY;
    private int wheelDelay = WHEEL_DELAY;
    private float multiplier = MULTIPLIER;

    private Robot robot;

    public MouseObject(Robot robot) {
        this.robot = robot;
    }

    public int getX() {
        return MouseEventsManager.getInstance().getX();
    }

    public int getY() {
        return MouseEventsManager.getInstance().getY();
    }


    // basics
    public void button(String button, String action) {
        switch (action) {
            case "PRESS":
                press(button);
                break;
            case "RELEASE":
                release(button);
                break;
            default:
                Out.println(String.format("Undefined mouse action '%s' in click method", action));
                return;
        }
    }

    public void buttonAt(String button, String action, int x, int y) {
        moveTo(x,y);
        button(button,action);
    }

    // movement
    public void moveTo(int x, int y) {
        if (x < 0 || y < 0) {
            Out.println(String.format("Negative coordinates '%s,%s' at moveTo method", x, y));
            return;
        }
        robot.mouseMove(x, y);
        robot.delay(getMultipliedMoveDelay());
    }

    public void move(int dx, int dy) {
        robot.mouseMove(getX() + dx, getY() + dy);
        robot.delay(getMultipliedMoveDelay());
    }

    public void setX(int x) throws ScriptException {
        if (x < 0) {
            Out.println(String.format("Negative coordinate '%s' at setX method", x));
            return;
        }
        robot.mouseMove(x, getY());
        robot.delay(getMultipliedMoveDelay());
    }

    public void setY(int y) {
        if (y < 0) {
            Out.println(String.format("Negative coordinate '%s' at setY method", y));
            return;
        }
        robot.mouseMove(getX(), y);
        robot.delay(getMultipliedMoveDelay());
    }

    // movement by path
    public void moveAbsolute(String path) {
        MouseMoveUtil mu = new MouseMoveUtil(path);
        int x;
        int y;
        while (mu.hasNext()) {
            x = mu.getNext();
            y = mu.getNext();
            robot.mouseMove(x, y);
            robot.delay(getMultipliedMoveDelay());
        }
    }

    public void moveAbsolute_D(String path) {
        MouseMoveUtil mu = new MouseMoveUtil(path);
        int x;
        int y;
        int delay;
        while (mu.hasNext()) {
            x = mu.getNext();
            y = mu.getNext();
            delay = mu.getNext();
            robot.mouseMove(x, y);
            robot.delay(multiply(delay));
        }
    }

    public void moveRelative(String path) {
        MouseMoveUtil mu = new MouseMoveUtil(path);
        int dx;
        int dy;
        while (mu.hasNext()) {
            dx = mu.getNext();
            dy = mu.getNext();
            robot.mouseMove(getX() + dx, getY() + dy);
            robot.delay(getMultipliedMoveDelay());
        }
    }

    public void moveRelative_D(String path) {
        MouseMoveUtil mu = new MouseMoveUtil(path);
        int dx;
        int dy;
        int delay;
        while (mu.hasNext()) {
            dx = mu.getNext();
            dy = mu.getNext();
            delay = mu.getNext();
            robot.mouseMove(getX() + dx, getY() + dy);
            robot.delay(multiply(delay));
        }
    }

    // click

    /**
     * Clicks mouse button
     *
     * @param button - name of the mouse button that should be clicked
     */
    public void click(String button) {
        int buttonEventCode = MouseCodes.getEventCodeByName(button);
        if (buttonEventCode == -1) {
            Out.println(String.format("Undefined mouse button '%s' in click method", button));
            return;
        }
        robot.mousePress(buttonEventCode);
        robot.delay(getMultipliedPressDelay());
        robot.mouseRelease(buttonEventCode);
        robot.delay(getMultipliedReleaseDelay());
    }

    public void clickAt(String button, int x, int y) {
        moveTo(x, y);
        click(button);
    }

    public void moveAndClick(String button, int dx, int dy) {
        move(dx, dy);
        click(button);
    }

    // press

    /**
     * Presses mouse button
     *
     * @param button - name of the mouse button that should be pressed
     */
    public void press(String button) {
        int buttonEventCode = MouseCodes.getEventCodeByName(button);
        if (buttonEventCode == -1) {
            Out.println(String.format("Undefined mouse button '%s' in press method", button));
            return;
        }
        robot.mousePress(buttonEventCode);
        robot.delay(getMultipliedPressDelay());
    }

    public void pressAt(String button, int x, int y) {
        moveTo(x, y);
        press(button);
    }

    public void moveAndPress(String button, int dx, int dy) {
        move(dx, dy);
        press(button);
    }

    // release

    /**
     * Releases mouse button
     *
     * @param button - name of the button that should be released
     */
    public void release(String button) {
        int buttonEventCode = MouseCodes.getEventCodeByName(button);
        if (buttonEventCode == -1) {
            Out.println(String.format("Undefined mouse button '%s' in release method", button));
            return;
        }
        robot.mouseRelease(buttonEventCode);
        robot.delay(getMultipliedReleaseDelay());
    }

    public void releaseAt(String button, int x, int y) {
        moveTo(x, y);
        release(button);
    }

    public void moveAndRelease(String button, int dx, int dy) {
        move(dx, dy);
        release(button);
    }

    /**
     * Rotates mouse wheel
     *
     * @param direction - 'UP' or 'DOWN'
     * @param amount    - any non negative number
     */
    public void wheel(String direction, int amount) {
        if (amount < 0) {
            Out.println(String.format("Wheel amount '%s' can't be less then 0", amount));
            return;
        }

        switch (direction) {
            case "DOWN":
                robot.mouseWheel(amount);
                robot.delay(getMultipliedWheelDelay());
                break;
            case "UP":
                robot.mouseWheel(amount * -1);
                robot.delay(getMultipliedWheelDelay());
                break;
            default:
                Out.println(String.format("Wrong wheel direction '%s'", direction));
                break;
        }
    }

    public void wheelAt(String direction, int amount, int x, int y) {
        moveTo(x, y);
        wheel(direction, amount);
    }

    public void moveAndWheel(String direction, int amount, int dx, int dy) {
        move(dx, dy);
        wheel(direction, amount);
    }
    // Getters\setters

    /**
     * @return the pressDelay
     */
    public int getPressDelay() {
        return pressDelay;
    }

    /**
     * @param pressDelay the pressDelay to set
     */
    public void setPressDelay(int pressDelay) {
        if (pressDelay < 0) {
            this.pressDelay = 0;
        } else {
            this.pressDelay = pressDelay;
        }
    }

    /**
     * @return the releaseDelay
     */
    public int getReleaseDelay() {
        return releaseDelay;
    }

    /**
     * @param releaseDelay the releaseDelay to set
     */
    public void setReleaseDelay(int releaseDelay) {
        if (releaseDelay < 0) {
            this.releaseDelay = 0;
        } else {
            this.releaseDelay = releaseDelay;
        }
    }


    /**
     * @return the moveDelay
     */
    public int getMoveDelay() {
        return moveDelay;
    }

    /**
     * @param moveDelay the moveDelay to set
     */
    public void setMoveDelay(int moveDelay) {
        if (moveDelay < 0) {
            this.moveDelay = 0;
        } else {
            this.moveDelay = moveDelay;
        }
    }

    public int getWheelDelay() {
        return wheelDelay;
    }

    public void setWheelDelay(int wheelDelay) {
        if (wheelDelay < 0) {
            this.wheelDelay = 0;
        } else {
            this.wheelDelay = wheelDelay;
        }
    }

    public float getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(float multiplier) {
        if (multiplier < 0) {
            this.multiplier = 0;
        } else {
            this.multiplier = multiplier;
        }
    }

    public void resetMultiplier() {
        multiplier = MULTIPLIER;
    }

    public float getSpeed() {
        return 1f / getMultiplier();
    }

    public void setSpeed(float multiplier) {
        setMultiplier(1f / multiplier);
    }

    public void resetSpeed() {
        resetMultiplier();
    }

    public void setDelays(int delay) {
        this.moveDelay = delay;
        this.pressDelay = delay;
        this.releaseDelay = delay;
        this.wheelDelay = delay;
    }

    public void resetDelays() {
        this.moveDelay = MOVE_DELAY;
        this.pressDelay = PRESS_DELAY;
        this.releaseDelay = RELEASE_DELAY;
        this.wheelDelay = WHEEL_DELAY;
    }

    //////////////////// private

    private int multiply(int delay) {
        return Math.round(delay * multiplier);
    }

    private int getMultipliedPressDelay() {
        return (int) (pressDelay * multiplier);
    }

    private int getMultipliedReleaseDelay() {
        return (int) (releaseDelay * multiplier);
    }

    private int getMultipliedMoveDelay() {
        return (int) (moveDelay * multiplier);
    }

    private int getMultipliedWheelDelay() {
        return (int) (wheelDelay * multiplier);
    }
}
