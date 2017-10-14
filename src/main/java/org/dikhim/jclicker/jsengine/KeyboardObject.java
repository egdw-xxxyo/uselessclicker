package org.dikhim.jclicker.jsengine;

import java.awt.Robot;

import org.dikhim.jclicker.events.KeyCodes;

/**
 * Created by dikobraz on 31.03.17.
 */
public class KeyboardObject {
	private int defaultDelay  =10;
    private Robot robot;
    private JSEngine engine;
    public KeyboardObject(JSEngine engine) {
    	this.setEngine(engine);
        this.robot = engine.getRobot();
    }
	public void setDefaultDelay(int ms) {
		if(ms>=0)defaultDelay=ms;
	}
	public int getDefaultDelay() {
		return defaultDelay;
	}
    public void press(String keyName){
        robot.keyPress(KeyCodes.getEventCodeByName(keyName));
        robot.delay(defaultDelay);
    }
    public void release(String keyName){
        robot.keyRelease(KeyCodes.getEventCodeByName(keyName));
        robot.delay(defaultDelay);
    }
	public JSEngine getEngine() {
		return engine;
	}
	public void setEngine(JSEngine engine) {
		this.engine = engine;
	}
}
