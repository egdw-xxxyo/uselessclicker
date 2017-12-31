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
@SuppressWarnings({"unused", "WeakerAccess"})
public class KeyboardObject {

    // Constants
    private final int PRESS_DELAY = 10;
    private final int RELEASE_DELAY = 10;
    private final float MULTIPLIER = 1f;

    private int pressDelay = PRESS_DELAY;
    private int releaseDelay = RELEASE_DELAY;
    private float multiplier = MULTIPLIER;


    private Robot robot;

    public KeyboardObject(JSEngine engine) {
        this.robot = engine.getRobot();
    }

    public KeyboardObject(Robot robot) {
        this.robot = robot;
    }

    public void press(String keys) {
        Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
        for (String key : keySet) {
            int keyCode = KeyCodes.getEventCodeByName(key);
            if (keyCode != -1) {
                robot.keyPress(keyCode);
                robot.delay(getMultipliedPressDelay());
            } else {
                Out.println(String.format("Undefined key '%s' in press method", key));
            }
        }
    }

    public void release(String keys) {
        Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
        for (String key : keySet) {
            int keyCode = KeyCodes.getEventCodeByName(key);
            if (keyCode != -1) {
                robot.keyRelease(keyCode);
                robot.delay(getMultipliedReleaseDelay());
            } else {
                Out.println(String.format("Undefined key '%s' in release method", key));
            }
        }
    }

    public void type(String keys) {
        Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
        for (String key : keySet) {
            int keyCode = KeyCodes.getEventCodeByName(key);
            if (keyCode != -1) {
                robot.keyPress(keyCode);
                robot.delay(getMultipliedPressDelay());
                robot.keyRelease(keyCode);
                robot.delay(getMultipliedReleaseDelay());
            } else {
                Out.println(String.format("Undefined key '%s' in type method", key));
            }
        }
    }

    public int getPressDelay() {
        return pressDelay;
    }

    public void setPressDelay(int pressDelay) {
        if (pressDelay < 0) {
            this.pressDelay = 0;
        } else {
            this.pressDelay = pressDelay;
        }
    }

    public int getReleaseDelay() {
        return releaseDelay;
    }

    public void setReleaseDelay(int releaseDelay) {
        if (releaseDelay < 0) {
            this.releaseDelay = 0;
        } else {
            this.releaseDelay = releaseDelay;
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
        this.multiplier = MULTIPLIER;
    }

    public float getSpeed() {
        return 1f/getMultiplier();
    }

    public void setSpeed(float multiplier) {
        setMultiplier(1f/multiplier);
    }

    public void resetSpeed() {
        resetMultiplier();
    }
    public void setDelays(int delay) {
        setPressDelay(delay);
        setReleaseDelay(delay);
    }

    public void resetDelays() {
        this.pressDelay = PRESS_DELAY;
        this.releaseDelay = RELEASE_DELAY;
    }

    public boolean isPressed(String keys) {
        Set<String> keySet = new HashSet<>(Arrays.asList(keys.split(" ")));
        return KeyEventsManager.getInstance().isPressed(keySet);
    }

    /// private

    private int getMultipliedPressDelay() {
        return (int) (pressDelay * multiplier);
    }

    private int getMultipliedReleaseDelay() {
        return (int) (releaseDelay * multiplier);
    }

}
