package org.dikhim.jclicker.jsengine;

import java.awt.Robot;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.dikhim.jclicker.events.KeyCodes;
import org.dikhim.jclicker.events.KeyEventsManager;
import org.dikhim.jclicker.util.output.Out;

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
	public KeyboardObject(Robot robot) {
		this.robot = robot;
	}

	public void press(String keys) {		
		Set<String> keySet = new LinkedHashSet<String>(Arrays.asList(keys.split(" ")));
		for(String key:keySet) {
			int keyCode = KeyCodes.getEventCodeByName(key);
			if(keyCode !=-1){
				robot.keyPress(keyCode);
				robot.delay(pressDelay);
			}else {
				Out.println("Method call failed: key.press('"+keys+"')");
			}
		}
	}
	public void release(String keys) {		
		Set<String> keySet = new LinkedHashSet<String>(Arrays.asList(keys.split(" ")));
		for(String key:keySet) {
			int keyCode = KeyCodes.getEventCodeByName(key);
			if(keyCode !=-1){
				robot.keyRelease(keyCode);
				robot.delay(releaseDelay);
			}else {
				Out.println("Method call failed: key.release('"+keys+"')");
			}
		}
	}
	public void type(String keys) {
		Set<String> keySet = new LinkedHashSet<String>(Arrays.asList(keys.split(" ")));
		for(String key:keySet) {
			int keyCode = KeyCodes.getEventCodeByName(key);
			if(keyCode !=-1){
				robot.keyPress(keyCode);
				robot.delay(pressDelay);
				robot.keyRelease(keyCode);
				robot.delay(releaseDelay);
			}else {
				Out.println("Method call failed: key.type('"+keys+"')");
			}
		}
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
	
	public boolean isPressed(String keys) {
		Set<String> keySet = new HashSet<String>(Arrays.asList(keys.split(" ")));
		return KeyEventsManager.getInstance().isPressed(keySet);
	}

}
