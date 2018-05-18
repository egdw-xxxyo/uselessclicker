package org.dikhim.jclicker.jsengine.objects;


import org.dikhim.jclicker.actions.utils.MouseCodes;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.jsengine.robot.Robot;
import org.dikhim.jclicker.util.MouseMoveUtil;
import org.dikhim.jclicker.util.Out;

/**
 *
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsMouseObject implements MouseObject {

    // Constants
    private final int PRESS_DELAY = 10;
    private final int RELEASE_DELAY = 10;
    private final int MOVE_DELAY = 10;
    private final int WHEEL_DELAY = 10;
    private final float MULTIPLIER = 1;
    private final int MIN_DELAY = 5;

    private int pressDelay = PRESS_DELAY;
    private int releaseDelay = RELEASE_DELAY;
    private int moveDelay = MOVE_DELAY;
    private int wheelDelay = WHEEL_DELAY;
    private float multiplier = MULTIPLIER;
    private int minDelay = MIN_DELAY;


    private Robot robot;
    private final Object monitor;

    public JsMouseObject(Robot robot) {
        this.robot = robot;
        this.monitor = robot.getMonitor();
    }

    public int getX() {
        synchronized (monitor) {
            return MouseEventsManager.getInstance().getX();
        }
    }

    public int getY() {
        synchronized (monitor) {
            return MouseEventsManager.getInstance().getY();
        }
    }


    // basics
    public void button(String button, String action) {
        synchronized (monitor) {
            switch (action) {
                case "PRESS":
                    press(button);
                    break;
                case "RELEASE":
                    release(button);
                    break;
                case "CLICK":
                    click(button);
                    break;
                default:
                    Out.println(String.format("Undefined mouse actions '%s' in button method", action));
            }
        }
    }


    public void buttonAt(String button, String action, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            button(button, action);
        }
    }

    // movement

    public void move(int dx, int dy) {
        synchronized (monitor) {
            robot.mouseMove(getX() + dx, getY() + dy);
            delay(getMultipliedMoveDelay());
        }
    }

    public void moveAndButton(String button, String action, int dx, int dy) {
        synchronized (monitor) {
            move(dx, dy);
            button(button, action);
        }
    }

    public void moveTo(int x, int y) {
        synchronized (monitor) {
            if (x < 0 || y < 0) {
                Out.println(String.format("Negative coordinates '%s,%s' at moveTo method", x, y));
                return;
            }
            robot.mouseMove(x, y);
            delay(getMultipliedMoveDelay());
        }
    }

    public void setX(int x) {
        synchronized (monitor) {
            if (x < 0) {
                Out.println(String.format("Negative coordinate '%s' at setX method", x));
                return;
            }
            robot.mouseMove(x, getY());
            delay(getMultipliedMoveDelay());
        }
    }

    public void setY(int y) {
        synchronized (monitor) {
            if (y < 0) {
                Out.println(String.format("Negative coordinate '%s' at setY method", y));
                return;
            }
            robot.mouseMove(getX(), y);
            delay(getMultipliedMoveDelay());
        }
    }

    // movement by path
    public void moveAbsolute(String path) {
        synchronized (monitor) {
            MouseMoveUtil mu = new MouseMoveUtil(path);
            int x;
            int y;
            while (mu.hasNext()) {
                x = mu.getNext();
                y = mu.getNext();
                robot.mouseMove(x, y);
                delay(getMultipliedMoveDelay());
            }
        }
    }

    public void moveAbsolute_D(String path) {
        synchronized (monitor) {
            MouseMoveUtil mu = new MouseMoveUtil(path);
            int x;
            int y;
            int delay;
            while (mu.hasNext()) {
                x = mu.getNext();
                y = mu.getNext();
                delay = mu.getNext();
                robot.mouseMove(x, y);
                delay(multiply(delay));
            }
        }
    }

    public void moveRelative(String path) {
        synchronized (monitor) {
            MouseMoveUtil mu = new MouseMoveUtil(path);
            int dx;
            int dy;
            while (mu.hasNext()) {
                dx = mu.getNext();
                dy = mu.getNext();
                robot.mouseMove(getX() + dx, getY() + dy);
                delay(getMultipliedMoveDelay());
            }
        }
    }

    public void moveRelative_D(String path) {
        synchronized (monitor) {
            MouseMoveUtil mu = new MouseMoveUtil(path);
            int dx;
            int dy;
            int delay;
            while (mu.hasNext()) {
                dx = mu.getNext();
                dy = mu.getNext();
                delay = mu.getNext();
                robot.mouseMove(getX() + dx, getY() + dy);
                delay(multiply(delay));
            }
        }
    }

    // click

    /**
     * Clicks mouse button
     *
     * @param button - name of the mouse button that should be clicked
     */
    public void click(String button) {
        synchronized (monitor) {
            int buttonEventCode = MouseCodes.getEventCodeByName(button);
            if (buttonEventCode == -1) {
                Out.println(String.format("Undefined mouse button '%s' in click method", button));
                return;
            }
            robot.mousePress(buttonEventCode);
            delay(getMultipliedPressDelay());
            robot.mouseRelease(buttonEventCode);
            delay(getMultipliedReleaseDelay());
        }
    }

    public void clickAt(String button, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            click(button);
        }
    }

    public void moveAndClick(String button, int dx, int dy) {
        synchronized (monitor) {
            move(dx, dy);
            click(button);
        }
    }

    // press

    /**
     * Presses mouse button
     *
     * @param button - name of the mouse button that should be pressed
     */
    public void press(String button) {
        synchronized (monitor) {
            int buttonEventCode = MouseCodes.getEventCodeByName(button);
            if (buttonEventCode == -1) {
                Out.println(String.format("Undefined mouse button '%s' in press method", button));
                return;
            }
            robot.mousePress(buttonEventCode);
            delay(getMultipliedPressDelay());
        }
    }

    public void pressAt(String button, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            press(button);
        }
    }

    public void moveAndPress(String button, int dx, int dy) {
        synchronized (monitor) {
            move(dx, dy);
            press(button);
        }
    }

    // release

    /**
     * Releases mouse button
     *
     * @param button - name of the button that should be released
     */
    public void release(String button) {
        synchronized (monitor) {
            int buttonEventCode = MouseCodes.getEventCodeByName(button);
            if (buttonEventCode == -1) {
                Out.println(String.format("Undefined mouse button '%s' in release method", button));
                return;
            }
            robot.mouseRelease(buttonEventCode);
            delay(getMultipliedReleaseDelay());
        }
    }

    public void releaseAt(String button, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            release(button);
        }
    }

    public void moveAndRelease(String button, int dx, int dy) {
        synchronized (monitor) {
            move(dx, dy);
            release(button);
        }
    }

    public void moveAndWheel(String direction, int amount, int dx, int dy) {
        synchronized (monitor) {
            move(dx, dy);
            wheel(direction, amount);
        }
    }

    // Getters\setters

    /**
     * @return the pressDelay
     */
    public int getPressDelay() {
        synchronized (monitor) {
            return pressDelay;
        }
    }

    /**
     * @param pressDelay the pressDelay to set
     */
    public void setPressDelay(int pressDelay) {
        synchronized (monitor) {
            if (pressDelay < 0) {
                this.pressDelay = 0;
            } else {
                this.pressDelay = pressDelay;
            }
        }
    }

    /**
     * @return the releaseDelay
     */
    public int getReleaseDelay() {
        synchronized (monitor) {
            return releaseDelay;
        }
    }

    /**
     * @param releaseDelay the releaseDelay to set
     */
    public void setReleaseDelay(int releaseDelay) {
        synchronized (monitor) {
            if (releaseDelay < 0) {
                this.releaseDelay = 0;
            } else {
                this.releaseDelay = releaseDelay;
            }
        }
    }


    /**
     * @return the moveDelay
     */
    public int getMoveDelay() {
        synchronized (monitor) {
            return moveDelay;
        }
    }

    /**
     * @param moveDelay the moveDelay to set
     */
    public void setMoveDelay(int moveDelay) {
        synchronized (monitor) {
            if (moveDelay < 0) {
                this.moveDelay = 0;
            } else {
                this.moveDelay = moveDelay;
            }
        }
    }

    public int getWheelDelay() {
        synchronized (monitor) {
            return wheelDelay;
        }
    }

    public void setWheelDelay(int wheelDelay) {
        synchronized (monitor) {
            if (wheelDelay < 0) {
                this.wheelDelay = 0;
            } else {
                this.wheelDelay = wheelDelay;
            }
        }
    }

    public float getMultiplier() {
        synchronized (monitor) {
            return multiplier;
        }
    }

    public void setMultiplier(float multiplier) {
        synchronized (monitor) {
            if (multiplier < 0) {
                this.multiplier = 0;
            } else {
                this.multiplier = multiplier;
            }
        }
    }

    public void resetMultiplier() {
        synchronized (monitor) {
            multiplier = MULTIPLIER;
        }
    }

    public float getSpeed() {
        synchronized (monitor) {
            return 1f / getMultiplier();
        }
    }

    public void setSpeed(float multiplier) {
        synchronized (monitor) {
            setMultiplier(1f / multiplier);
        }
    }

    public void resetSpeed() {
        synchronized (monitor) {
            resetMultiplier();
        }
    }

    public void setDelays(int delay) {
        synchronized (monitor) {
            setPressDelay(delay);
            setReleaseDelay(delay);
            setMoveDelay(delay);
            setWheelDelay(delay);
        }
    }

    public void resetDelays() {
        synchronized (monitor) {
            this.moveDelay = MOVE_DELAY;
            this.pressDelay = PRESS_DELAY;
            this.releaseDelay = RELEASE_DELAY;
            this.wheelDelay = WHEEL_DELAY;
            this.minDelay = MIN_DELAY;
        }
    }

    public int getMinDelay() {
        synchronized (monitor) {
            return minDelay;
        }
    }

    public void setMinDelay(int minDelay) {
        synchronized (monitor) {
            this.minDelay = minDelay;
        }
    }


    /**
     * Rotates mouse wheel
     *
     * @param direction - 'UP' or 'DOWN'
     * @param amount    - any non negative number
     */
    public void wheel(String direction, int amount) {
        synchronized (monitor) {
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
    }


    public void wheelIgnoringDelays(String direction, int amount) {
        synchronized (monitor) {
            int tmpWheelDelay = getWheelDelay();
            int tmpMinDelay = getMinDelay();
            try {
                setWheelDelay(0);
                setMinDelay(0);
                wheel(direction, amount);
            } finally {
                setWheelDelay(tmpWheelDelay);
                setMinDelay(tmpMinDelay);
            }
        }
    }

    public void wheelAt(String direction, int amount, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            wheel(direction, amount);
        }
    }

    public int getMultipliedPressDelay() {
        synchronized (monitor) {
            return checkDelay((int) (pressDelay * multiplier));
        }
    }

    public int getMultipliedReleaseDelay() {
        synchronized (monitor) {
            return checkDelay((int) (releaseDelay * multiplier));
        }
    }

    public int getMultipliedMoveDelay() {
        synchronized (monitor) {
            return checkDelay((int) (moveDelay * multiplier));
        }
    }

    public int getMultipliedWheelDelay() {
        synchronized (monitor) {
            return checkDelay((int) (wheelDelay * multiplier));
        }
    }
    
    // private
    private int multiply(int delay) {
        return checkDelay(Math.round(delay * multiplier));
    }

    private int checkDelay(int delay) {
        synchronized (monitor) {
            if (delay < minDelay) return minDelay;

            return delay;
        }
    }

    private void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
