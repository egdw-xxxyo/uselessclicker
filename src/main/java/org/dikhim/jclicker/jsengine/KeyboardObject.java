package org.dikhim.jclicker.jsengine;

import java.awt.Robot;

import org.dikhim.jclicker.events.KeyCodes;

/**
 * Created by dikobraz on 31.03.17.
 */
public class KeyboardObject {

	// Constants
	private final int PRESS_DELAY = 10;
	private final int RELEASE_DELAY = 10;

	private int pressDelay = PRESS_DELAY;
	private int releaseDelay = RELEASE_DELAY;

	private Robot robot;
	public KeyboardObject(JSEngine engine) {
		this.robot = engine.getRobot();
	}

	public void press(String keyName) {
		robot.keyPress(KeyCodes.getEventCodeByName(keyName));
		robot.delay(pressDelay);
	}
	public void release(String keyName) {
		robot.keyRelease(KeyCodes.getEventCodeByName(keyName));
		robot.delay(releaseDelay);
	}
	public void type(String key) {
		int code = KeyCodes.getEventCodeByName(key);
		robot.keyPress(code);
		robot.delay(pressDelay);
		robot.keyRelease(code);
		robot.delay(releaseDelay);
	}

	public int getPressDelay() {
		return pressDelay;
	}

	public void setPressDelay(int pressDelay) {
		this.pressDelay = pressDelay;
	}

	public int getReleaseDelay() {
		return releaseDelay;
	}

	public void setReleaseDelay(int releaseDelay) {
		this.releaseDelay = releaseDelay;
	}

}
