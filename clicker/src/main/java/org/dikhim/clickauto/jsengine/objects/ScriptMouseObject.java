package org.dikhim.clickauto.jsengine.objects;


import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.jsengine.utils.MouseCodes;
import org.dikhim.clickauto.util.MathUtil;
import org.dikhim.clickauto.util.logger.ClickAutoLog;

import java.awt.*;

public class ScriptMouseObject implements MouseObject {
    protected final int PRESS_DELAY = 10;
    protected final int RELEASE_DELAY = 10;
    protected final int MOVE_DELAY = 10;
    protected final int WHEEL_DELAY = 10;
    protected final float MULTIPLIER = 1;
    protected final int MIN_DELAY = 5;

    protected final Robot robot;
    public final AnimatedMouse animated;

    protected volatile int pressDelay = PRESS_DELAY;
    protected volatile int releaseDelay = RELEASE_DELAY;
    protected volatile int moveDelay = MOVE_DELAY;
    protected volatile int wheelDelay = WHEEL_DELAY;
    protected volatile double multiplier = MULTIPLIER;
    protected volatile int minDelay = MIN_DELAY;

    public ScriptMouseObject(Robot robot) {
        this.robot = robot;
        this.animated = new AnimatedMouse(this, robot);
    }


    // basic

    protected void press(String button) {
        int buttonEventCode = MouseCodes.getEventCodeByName(button);
        if (buttonEventCode == -1) {
            ClickAutoLog.get().error("Undefined mouse button '%s' in press method\n", button);
            return;
        }
        robot.mousePress(buttonEventCode);
        delay(getMultipliedPressDelay());
    }

    protected void release(String button) {
        int buttonEventCode = MouseCodes.getEventCodeByName(button);
        if (buttonEventCode == -1) {
            ClickAutoLog.get().error("Undefined mouse button '%s' in release method\n", button);
            return;
        }
        robot.mouseRelease(buttonEventCode);
        delay(getMultipliedReleaseDelay());
    }

    protected void click(String buttons) {
        String[] buttonList = buttons.split(" ");
        for (String btn : buttonList) {
            int buttonEventCode = MouseCodes.getEventCodeByName(btn);
            if (buttonEventCode == -1) {
                ClickAutoLog.get().error("Undefined mouse button '%s' in click method\n", btn);
                return;
            }
            robot.mousePress(buttonEventCode);
            delay(getMultipliedPressDelay());
            robot.mouseRelease(buttonEventCode);
            delay(getMultipliedReleaseDelay());
        }
    }

    protected void wheel(String direction, int amount) {

        if (amount < 0) {
            ClickAutoLog.get().error("Wheel amount '%s' can't be less then 0\n", amount);
            return;
        }

        switch (direction) {
            case "DOWN":
                robot.mouseWheel(amount);
                delay(getMultipliedWheelDelay());
                break;
            case "UP":
                robot.mouseWheel(amount * -1);
                delay(getMultipliedWheelDelay());
                break;
            default:
                ClickAutoLog.get().error("Wrong wheel direction '%s'\n", direction);
                break;
        }

    }

    protected int checkDelay(int delay) {
        if (delay < minDelay) return minDelay;
        return delay;
    }

    protected void delay(int delay) {
        if (delay < 0) delay = 0;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public AnimatedMouse animated() {
        return animated;
    }

    @Override
    public int getX() {
        synchronized (robot) {
            return MouseInfo.getPointerInfo().getLocation().x;
        }
    }

    @Override
    public int getY() {
        synchronized (robot) {
            return MouseInfo.getPointerInfo().getLocation().y;
        }
    }

    @Override
    public void move(int dx, int dy) {
        synchronized (robot) {
            robot.mouseMove(getX() + dx, getY() + dy);
            delay(getMultipliedMoveDelay());
        }
    }

    @Override
    public void moveTo(int x, int y) {
        synchronized (robot) {
            if (x < 0 || y < 0) {
                ClickAutoLog.get().error("Negative coordinates '%s,%s' at moveTo method\n", x, y);
                return;
            }
            robot.mouseMove(x, y);
            delay(getMultipliedMoveDelay());
        }
    }

    @Override
    public void setX(int x) {
        synchronized (robot) {
            if (x < 0) {
                ClickAutoLog.get().error("Negative coordinate '%s' at setX method\n", x);
                return;
            }
            robot.mouseMove(x, getY());
            delay(getMultipliedMoveDelay());
        }
    }

    @Override
    public void setY(int y) {
        synchronized (robot) {
            if (y < 0) {
                ClickAutoLog.get().error("Negative coordinate '%s' at setY method\n", y);
                return;
            }
            robot.mouseMove(getX(), y);
            delay(getMultipliedMoveDelay());
        }
    }

    // delays

    /**
     * @return the pressDelay
     */
    @Override
    public int getPressDelay() {
        synchronized (robot) {
            return pressDelay;
        }
    }

    /**
     * @param pressDelay the pressDelay to set
     */
    @Override
    public void setPressDelay(int pressDelay) {
        synchronized (robot) {
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
        synchronized (robot) {
            return releaseDelay;
        }
    }

    /**
     * @param releaseDelay the releaseDelay to set
     */
    @Override
    public void setReleaseDelay(int releaseDelay) {
        synchronized (robot) {
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
        synchronized (robot) {
            return moveDelay;
        }
    }

    /**
     * @param moveDelay the moveDelay to set
     */
    @Override
    public void setMoveDelay(int moveDelay) {
        synchronized (robot) {
            if (moveDelay < 0) {
                this.moveDelay = 0;
            } else {
                this.moveDelay = moveDelay;
            }
        }
    }

    @Override
    public int getWheelDelay() {
        synchronized (robot) {
            return wheelDelay;
        }
    }

    @Override
    public void setWheelDelay(int wheelDelay) {
        synchronized (robot) {
            if (wheelDelay < 0) {
                this.wheelDelay = 0;
            } else {
                this.wheelDelay = wheelDelay;
            }
        }
    }

    @Override
    public double getMultiplier() {
        synchronized (robot) {
            return multiplier;
        }
    }

    @Override
    public void setMultiplier(double multiplier) {
        synchronized (robot) {
            if (multiplier < 0) {
                this.multiplier = 0;
            } else {
                this.multiplier = multiplier;
            }
        }
    }

    @Override
    public void resetMultiplier() {
        synchronized (robot) {
            multiplier = MULTIPLIER;
        }
    }

    @Override
    public double getSpeed() {
        synchronized (robot) {
            if (multiplier == 0) return 999999999;
            return MathUtil.roundTo1(1.0 / multiplier);
        }
    }

    @Override
    public void setSpeed(double speed) {
        synchronized (robot) {
            if (speed < 0.1) {
                speed = 0.1;
            }
            speed = MathUtil.roundTo1(speed);
            setMultiplier(1f / speed);
        }
    }

    @Override
    public void resetSpeed() {
        synchronized (robot) {
            resetMultiplier();
        }
    }

    @Override
    public void setDelays(int delay) {
        synchronized (robot) {
            setPressDelay(delay);
            setReleaseDelay(delay);
            setMoveDelay(delay);
            setWheelDelay(delay);
        }
    }

    @Override
    public void resetDelays() {
        synchronized (robot) {
            this.moveDelay = MOVE_DELAY;
            this.pressDelay = PRESS_DELAY;
            this.releaseDelay = RELEASE_DELAY;
            this.wheelDelay = WHEEL_DELAY;
            this.minDelay = MIN_DELAY;
        }
    }

    @Override
    public int getMinDelay() {
        synchronized (robot) {
            return minDelay;
        }
    }

    @Override
    public void setMinDelay(int minDelay) {
        synchronized (robot) {
            this.minDelay = minDelay;
        }
    }

    @Override
    public int getMultipliedPressDelay() {
        synchronized (robot) {
            return checkDelay((int) (pressDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedReleaseDelay() {
        synchronized (robot) {
            return checkDelay((int) (releaseDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedMoveDelay() {
        synchronized (robot) {
            return checkDelay((int) (moveDelay * multiplier));
        }
    }

    @Override
    public int getMultipliedWheelDelay() {
        synchronized (robot) {
            return checkDelay((int) (wheelDelay * multiplier));
        }
    }

    // basic

    @Override
    public void pressLeft() {
        synchronized (robot) {
            press("LEFT");
        }
    }

    @Override
    public void pressRight() {
        synchronized (robot) {
            press("RIGHT");
        }
    }

    @Override
    public void pressMiddle() {
        synchronized (robot) {
            press("MIDDLE");
        }
    }

    @Override
    public void releaseLeft() {
        synchronized (robot) {
            release("LEFT");
        }
    }

    @Override
    public void releaseRight() {
        synchronized (robot) {
            release("RIGHT");
        }
    }

    @Override
    public void releaseMiddle() {
        synchronized (robot) {
            release("MIDDLE");
        }
    }

    @Override
    public void clickLeft() {
        synchronized (robot) {
            click("LEFT");
        }
    }

    @Override
    public void clickRight() {
        synchronized (robot) {
            click("RIGHT");
        }
    }

    @Override
    public void clickMiddle() {
        synchronized (robot) {
            click("MIDDLE");
        }
    }

    @Override
    public void wheelUp(int amount) {
        synchronized (robot) {
            wheel("UP", amount);
        }
    }

    @Override
    public void wheelDown(int amount) {
        synchronized (robot) {
            wheel("DOWN", amount);
        }
    }

    @Override
    public void pressLeftAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            press("LEFT");
        }
    }

    @Override
    public void pressRightAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            press("RIGHT");
        }
    }

    @Override
    public void pressMiddleAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            press("MIDDLE");
        }
    }

    @Override
    public void releaseLeftAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            release("LEFT");
        }
    }

    @Override
    public void releaseRightAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            release("RIGHT");
        }
    }

    @Override
    public void releaseMiddleAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            release("MIDDLE");
        }
    }

    @Override
    public void clickLeftAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            click("LEFT");
        }
    }

    @Override
    public void clickRightAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            click("RIGHT");
        }
    }

    @Override
    public void clickMiddleAt(int x, int y) {
        synchronized (robot) {
            moveTo(x, y);
            click("MIDDLE");
        }
    }

    @Override
    public void wheelUpAt(int x, int y, int amount) {
        synchronized (robot) {
            moveTo(x, y);
            wheel("UP", amount);
        }
    }

    @Override
    public void wheelDownAt(int x, int y, int amount) {
        synchronized (robot) {
            moveTo(x, y);
            wheel("DOWN", amount);
        }
    }

    @Override
    public void moveAndPressLeft(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            press("LEFT");
        }
    }

    @Override
    public void moveAndPressRight(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            press("RIGHT");
        }
    }

    @Override
    public void moveAndPressMiddle(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            press("MIDDLE");
        }
    }

    @Override
    public void moveAndReleaseLeft(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            release("LEFT");
        }
    }

    @Override
    public void moveAndReleaseRight(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            release("RIGHT");
        }
    }

    @Override
    public void moveAndReleaseMiddle(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            release("MIDDLE");
        }
    }

    @Override
    public void moveAndClickLeft(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            click("LEFT");
        }
    }

    @Override
    public void moveAndClickRight(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            click("RIGHT");
        }
    }

    @Override
    public void moveAndClickMiddle(int dx, int dy) {
        synchronized (robot) {
            move(dx, dy);
            click("MIDDLE");
        }
    }

    @Override
    public void moveAndWheelUp(int dx, int dy, int amount) {
        synchronized (robot) {
            move(dx, dy);
            wheel("UP", amount);
        }
    }

    @Override
    public void moveAndWheelDown(int dx, int dy, int amount) {
        synchronized (robot) {
            move(dx, dy);
            wheel("DOWN", amount);
        }
    }
}
