package org.dikhim.jclicker.jsengine;

import java.awt.Robot;

import org.dikhim.jclicker.events.MouseCodes;
import org.dikhim.jclicker.events.MouseEventsManager;
import org.dikhim.jclicker.util.MouseMoveUtil;
import org.dikhim.jclicker.util.output.Out;

/**
 *
 */
public class MouseObject {

    // Constants
    private final int PRESS_DELAY = 10;
    private final int RELEASE_DELAY = 10;
    private final int MOVE_DELAY = 10;
    private final int WHEEL_DELAY =10;

    private int pressDelay = PRESS_DELAY;
    private int releaseDelay = RELEASE_DELAY;
    private int moveDelay = MOVE_DELAY;
    private int wheelDelay = WHEEL_DELAY;

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

    // movement
    public void moveTo(int x, int y) {
        robot.mouseMove(x, y);
        robot.delay(moveDelay);
    }

    public void move(int x, int y) {
        robot.mouseMove(getX() + x, getY() + y);
        robot.delay(moveDelay);
    }

    public void setX(int x) {
        robot.mouseMove(x, getY());
        robot.delay(moveDelay);
    }

    public void setY(int y) {
        robot.mouseMove(getX(), y);
        robot.delay(moveDelay);
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
            robot.delay(moveDelay);
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
            robot.delay(delay);
        }
    }

    public void moveRelative(String path) {
        int oldX = getX();
        int oldY = getY();
        MouseMoveUtil mu = new MouseMoveUtil(path);
        int x;
        int y;
        while (mu.hasNext()) {
            x = mu.getNext();
            y = mu.getNext();
            oldX += x;
            oldY += y;
            robot.mouseMove(oldX, oldY);
            robot.delay(moveDelay);
        }
    }

    public void moveRelative_D(String path) {
        int oldX = getX();
        int oldY = getY();
        MouseMoveUtil mu = new MouseMoveUtil(path);
        int x;
        int y;
        int delay;
        while (mu.hasNext()) {
            x = mu.getNext();
            y = mu.getNext();
            delay = mu.getNext();
            oldX += x;
            oldY += y;
            robot.mouseMove(oldX, oldY);
            robot.delay(delay);
        }
    }

    // click
    public void click(String key) {
        int code = MouseCodes.getEventCodeByName(key);
        robot.mousePress(code);
        robot.delay(pressDelay);
        robot.mouseRelease(code);
        robot.delay(releaseDelay);
    }

    public void clickAt(String key, int x, int y) {
        int code = MouseCodes.getEventCodeByName(key);
        robot.mouseMove(x, y);
        robot.delay(moveDelay);
        robot.mousePress(code);
        robot.delay(pressDelay);
        robot.mouseRelease(code);
        robot.delay(releaseDelay);
    }

    public void moveAndClick(String key, int x, int y) {
        int code = MouseCodes.getEventCodeByName(key);
        robot.mouseMove(getX() + x, getY() + y);
        robot.delay(moveDelay);
        robot.mousePress(code);
        robot.delay(pressDelay);
        robot.mouseRelease(code);
        robot.delay(releaseDelay);
    }

    // press
    public void press(String key) {
        robot.mousePress(MouseCodes.getEventCodeByName(key));
        robot.delay(pressDelay);
    }

    public void pressAt(String key, int x, int y) {
        robot.mouseMove(x, y);
        robot.delay(moveDelay);
        robot.mousePress(MouseCodes.getEventCodeByName(key));
        robot.delay(pressDelay);
    }

    public void moveAndPress(String key, int x, int y) {
        robot.mouseMove(getX() + x, getY() + y);
        robot.delay(moveDelay);
        robot.mousePress(MouseCodes.getEventCodeByName(key));
        robot.delay(pressDelay);
    }

    // release
    public void release(String key) {
        robot.mouseRelease(MouseCodes.getEventCodeByName(key));
        robot.delay(releaseDelay);
    }

    public void releaseAt(String key, int x, int y) {
        robot.mouseMove(x, y);
        robot.delay(moveDelay);
        robot.mouseRelease(MouseCodes.getEventCodeByName(key));
        robot.delay(releaseDelay);
    }

    public void moveAndRelease(String key, int x, int y) {
        robot.mouseMove(getX() + x, getY() + y);
        robot.delay(moveDelay);
        robot.mouseRelease(MouseCodes.getEventCodeByName(key));
        robot.delay(pressDelay);
    }

    public void wheel(String direction,int amount){
        if(amount<0){
            Out.println("MouseWheelAmount can't be less then 0  :'"+amount+"'");
            return;
        }

        if(direction.equals("DOWN")){
            robot.mouseWheel(amount);
        }else if(direction.equals("UP")){
            robot.mouseWheel(amount*-1);
        }else{
            Out.println("Wrong wheel direction  :'"+direction+"'");
        }
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
        this.pressDelay = pressDelay;
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
        this.releaseDelay = releaseDelay;
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
        this.moveDelay = moveDelay;
    }

    public int getWheelDelay() {
        return wheelDelay;
    }

    public void setWheelDelay(int wheelDelay) {
        this.wheelDelay = wheelDelay;
    }

    public void setAllDelays(int delay){
        this.moveDelay=delay;
        this.pressDelay=delay;
        this.releaseDelay=delay;
        this.wheelDelay=delay;
    }
}
