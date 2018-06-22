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

    @Override
    public int getX() {
        synchronized (monitor) {
            return MouseEventsManager.getInstance().getX();
        }
    }

    @Override
    public int getY() {
        synchronized (monitor) {
            return MouseEventsManager.getInstance().getY();
        }
    }


    // basics
    @Override
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

    @Override
    public void buttonAt(String button, String action, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            button(button, action);
        }
    }

    // movement
    @Override
    public void move(int dx, int dy) {
        synchronized (monitor) {
            robot.mouseMove(getX() + dx, getY() + dy);
            delay(getMultipliedMoveDelay());
        }
    }

    @Override
    public void moveAndButton(String button, String action, int dx, int dy) {
        synchronized (monitor) {
            move(dx, dy);
            button(button, action);
        }
    }

    @Override
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

    @Override
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

    @Override
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
    @Override
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

    @Override
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

    @Override
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

    @Override
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
     * @param buttons - name of the mouse button that should be clicked
     */
    @Override
    public void click(String buttons) {
        synchronized (monitor) {
            String[] buttonList = buttons.split(" ");
            for (String btn : buttonList) {
                int buttonEventCode = MouseCodes.getEventCodeByName(btn);
                if (buttonEventCode == -1) {
                    Out.println(String.format("Undefined mouse button '%s' in click method", btn));
                    return;
                }
                robot.mousePress(buttonEventCode);
                delay(getMultipliedPressDelay());
                robot.mouseRelease(buttonEventCode);
                delay(getMultipliedReleaseDelay());
            }
        }
    }

    @Override
    public void clickAt(String button, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            click(button);
        }
    }

    @Override
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
    @Override
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

    @Override
    public void pressAt(String button, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            press(button);
        }
    }

    @Override
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
    @Override
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

    @Override
    public void releaseAt(String button, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            release(button);
        }
    }

    @Override
    public void moveAndRelease(String button, int dx, int dy) {
        synchronized (monitor) {
            move(dx, dy);
            release(button);
        }
    }

    @Override
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
    @Override
    public int getPressDelay() {
        synchronized (monitor) {
            return pressDelay;
        }
    }

    /**
     * @param pressDelay the pressDelay to set
     */
    @Override
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
    @Override
    public int getReleaseDelay() {
        synchronized (monitor) {
            return releaseDelay;
        }
    }

    /**
     * @param releaseDelay the releaseDelay to set
     */
    @Override
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
    @Override
    public int getMoveDelay() {
        synchronized (monitor) {
            return moveDelay;
        }
    }

    /**
     * @param moveDelay the moveDelay to set
     */
    @Override
    public void setMoveDelay(int moveDelay) {
        synchronized (monitor) {
            if (moveDelay < 0) {
                this.moveDelay = 0;
            } else {
                this.moveDelay = moveDelay;
            }
        }
    }

    @Override
    public int getWheelDelay() {
        synchronized (monitor) {
            return wheelDelay;
        }
    }

    @Override
    public void setWheelDelay(int wheelDelay) {
        synchronized (monitor) {
            if (wheelDelay < 0) {
                this.wheelDelay = 0;
            } else {
                this.wheelDelay = wheelDelay;
            }
        }
    }

    @Override
    public float getMultiplier() {
        synchronized (monitor) {
            return multiplier;
        }
    }

    @Override
    public void setMultiplier(float multiplier) {
        synchronized (monitor) {
            if (multiplier < 0) {
                this.multiplier = 0;
            } else {
                this.multiplier = multiplier;
            }
        }
    }

    @Override
    public void resetMultiplier() {
        synchronized (monitor) {
            multiplier = MULTIPLIER;
        }
    }

    @Override
    public float getSpeed() {
        synchronized (monitor) {
            return 1f / getMultiplier();
        }
    }

    @Override
    public void setSpeed(float multiplier) {
        synchronized (monitor) {
            setMultiplier(1f / multiplier);
        }
    }

    @Override
    public void resetSpeed() {
        synchronized (monitor) {
            resetMultiplier();
        }
    }

    @Override
    public void setDelays(int delay) {
        synchronized (monitor) {
            setPressDelay(delay);
            setReleaseDelay(delay);
            setMoveDelay(delay);
            setWheelDelay(delay);
        }
    }

    @Override
    public void resetDelays() {
        synchronized (monitor) {
            this.moveDelay = MOVE_DELAY;
            this.pressDelay = PRESS_DELAY;
            this.releaseDelay = RELEASE_DELAY;
            this.wheelDelay = WHEEL_DELAY;
            this.minDelay = MIN_DELAY;
        }
    }

    @Override
    public int getMinDelay() {
        synchronized (monitor) {
            return minDelay;
        }
    }

    @Override
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
    @Override
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
    
    @Override
    public void wheelAt(String direction, int amount, int x, int y) {
        synchronized (monitor) {
            moveTo(x, y);
            wheel(direction, amount);
        }
    }

    @Override
    public int getMultipliedPressDelay() {
        synchronized (monitor) {
            return checkDelay((int) (pressDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedReleaseDelay() {
        synchronized (monitor) {
            return checkDelay((int) (releaseDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedMoveDelay() {
        synchronized (monitor) {
            return checkDelay((int) (moveDelay * multiplier));
        }
    }

    @Override
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
