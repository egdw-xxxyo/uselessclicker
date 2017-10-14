package org.dikhim.jclicker.jsengine;

import java.awt.Robot;
import java.awt.event.InputEvent;

import org.dikhim.jclicker.events.MouseCodes;
import org.dikhim.jclicker.events.MouseEventsManager;

/**
 * Created by dikobraz on 30.03.17.
 */
public class MouseObject {

	// Constants
	private final int PRESS_DELAY = 10;
	private final int RELEASE_DELAY = 10;
	private final int CLICK_DELAY = 10;
	private final int MOVE_DELAY = 10;
	
	private int pressDelay = PRESS_DELAY;
	private int releaseDelay = RELEASE_DELAY;
	private int clickDelay = CLICK_DELAY;
	private int moveDelay = MOVE_DELAY;
	
	private Robot robot;
	private JSEngine engine;

	public MouseObject(JSEngine engine) {
		this.setEngine(engine);
		this.robot = engine.getRobot();
	}

	public JSEngine getEngine() {
		return engine;
	}

	public void setEngine(JSEngine engine) {
		this.engine = engine;
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
		int oldX = getX();
		int oldY = getY();
		robot.mouseMove(oldX + x, oldY + y);
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
	// click
	public void click(String key) {
		int code = MouseCodes.getEventCodeByName(key);
		robot.mousePress(code);
		robot.delay(clickDelay);
		robot.mouseRelease(code);
		robot.delay(clickDelay);
	}

	public void clickAt(String key, int x, int y) {
		int code = MouseCodes.getEventCodeByName(key);
		robot.mouseMove(x, y);
		robot.delay(moveDelay);
		robot.mousePress(code);
		robot.delay(clickDelay);
		robot.mouseRelease(code);
		robot.delay(clickDelay);
	}

	// press
	public void press(String key) {
		robot.mousePress(MouseCodes.getEventCodeByName(key));
		robot.delay(pressDelay);
	}
	public void pressAt(String key, int x, int y) {
		robot.mouseMove(x, y);
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
		robot.mouseRelease(MouseCodes.getEventCodeByName(key));
		robot.delay(releaseDelay);
	}
	//Getters\setters

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
	 * @return the clickDelay
	 */
	public int getClickDelay() {
		return clickDelay;
	}

	/**
	 * @param clickDelay the clickDelay to set
	 */
	public void setClickDelay(int clickDelay) {
		this.clickDelay = clickDelay;
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

}
