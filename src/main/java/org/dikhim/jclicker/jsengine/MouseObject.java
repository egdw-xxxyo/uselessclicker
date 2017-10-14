package org.dikhim.jclicker.jsengine;

import java.awt.Robot;
import java.awt.event.InputEvent;

import org.dikhim.jclicker.events.MouseCodes;
import org.dikhim.jclicker.events.MouseEventsManager;

/**
 * Created by dikobraz on 30.03.17.
 */
public class MouseObject {
	private Robot robot;
	private JSEngine engine;
	private int defaultDelay =10;
	
	public MouseObject(JSEngine engine) {
		this.setEngine(engine);
		this.robot = engine.getRobot();
	}
	
	public void setDefaultDelay(int ms) {
		if(ms>=0)defaultDelay=ms;
	}
	public int getDefaultDelay() {
		return defaultDelay;
	}
	
	public JSEngine getEngine() {
		return engine;
	}

	public void setEngine(JSEngine engine) {
		this.engine = engine;
	}
	
	public void moveTo(int x, int y) {
		robot.mouseMove(x, y);
		robot.delay(defaultDelay);
	}
	public void press(String key) {
		robot.mousePress(MouseCodes.getEventCodeByName(key));
		robot.delay(defaultDelay);
	}
	public void release(String key) {
		robot.mouseRelease(MouseCodes.getEventCodeByName(key));
		robot.delay(defaultDelay);
	}
	public void pressAt(String key,int x,int y) {
		robot.mouseMove(x, y);
		robot.mousePress(MouseCodes.getEventCodeByName(key));
		robot.delay(defaultDelay);
	}
	public void releaseAt(String key,int x, int y) {
		robot.mouseMove(x, y);
		robot.mouseRelease(MouseCodes.getEventCodeByName(key));
		robot.delay(defaultDelay);
	}
	public void move(int x, int y) {
		int oldX = MouseEventsManager.getInstance().getX();
		int oldY = MouseEventsManager.getInstance().getY();
		moveTo(oldX + x, oldY + y);
	}
	public void setX(int x) {
		moveTo(x, getY());
	}
	public int getX() {
		return MouseEventsManager.getInstance().getX();
	}
	public void setY(int y) {
		moveTo(getX(), y);
	}
	public int getY() {
		return MouseEventsManager.getInstance().getY();
	}
	public void pressLeft() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(defaultDelay);
	}
	public void pressLeftAt(int x, int y) {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.delay(defaultDelay);
	}
	public void pressRight() {
		robot.mousePress(InputEvent.BUTTON2_MASK);
		robot.delay(defaultDelay);
	}
	public void pressRightAt(int x, int y) {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON2_MASK);
		robot.delay(defaultDelay);
	}
	public void pressMiddle() {
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.delay(defaultDelay);
	}
	public void pressMiddleAt(int x, int y) {
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON3_MASK);
		robot.delay(defaultDelay);
	}
	public void releaseLeft() {
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	public void releaseLeftAt(int x, int y) {
		robot.mouseMove(x, y);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
		robot.delay(defaultDelay);
	}
	public void releaseRight() {
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
		robot.delay(defaultDelay);
	}
	public void releaseRightAt(int x, int y) {
		robot.mouseMove(x, y);
		robot.mouseRelease(InputEvent.BUTTON2_MASK);
		robot.delay(defaultDelay);
	}
	public void releaseMiddle() {
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
		robot.delay(defaultDelay);
	}
	public void releaseMiddleAt(int x, int y) {
		robot.mouseMove(x, y);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
		robot.delay(defaultDelay);
	}



}
