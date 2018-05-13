package org.dikhim.jclicker.jsengine.objects;

import java.awt.Robot;
import java.util.*;

import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.util.output.Out;

/**
 * Created by dikobraz on 31.03.17.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JsKeyboardObject implements KeyboardObject {

    // Constants
    private final int PRESS_DELAY = 10;
    private final int RELEASE_DELAY = 10;
    private final float MULTIPLIER = 1f;
    private final int MIN_DELAY = 5;

    private int pressDelay = PRESS_DELAY;
    private int releaseDelay = RELEASE_DELAY;
    private float multiplier = MULTIPLIER;
    private int minDelay = MIN_DELAY;


    private Robot robot;

    public JsKeyboardObject(JSEngine engine) {
        this.robot = engine.getRobot();
    }

    public JsKeyboardObject(Robot robot) {
        this.robot = robot;
    }

    @Override
    public int getMinDelay() {
        return minDelay;
    }

    public float getMultiplier() {
        return multiplier;
    }

    public int getPressDelay() {
        return pressDelay;
    }

    public int getReleaseDelay() {
        return releaseDelay;
    }

    public float getSpeed() {
        return 1f / getMultiplier();
    }

    public boolean isPressed(String keys) {
        Set<String> keySet = new HashSet<>(Arrays.asList(keys.split(" ")));
        return KeyEventsManager.getInstance().isPressed(keySet);
    }

    public void perform(String keys, String action) {
        switch (action) {
            case "PRESS":
                press(keys);
                break;
            case "RELEASE":
                release(keys);
                break;
            case "TYPE":
                type(keys);
            default:
                Out.println(String.format("Undefined key actions '%s' in perform method", action));
        }
    }

    public void press(String keys) {
        Set<String> keySet = new LinkedHashSet<>(Arrays.asList(keys.split(" ")));
        for (String key : keySet) {
            int keyCode = KeyCodes.getEventCodeByName(key);
            if (keyCode != -1) {
                robot.keyPress(keyCode);
                robot.delay(getMultipliedPressDelay());
            } else {
                Out.println(String.format("Undefined key '%s'in sequence '%s' in press method", key, keys));
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

    public void resetDelays() {
        this.pressDelay = PRESS_DELAY;
        this.releaseDelay = RELEASE_DELAY;
    }

    public void resetMultiplier() {
        this.multiplier = MULTIPLIER;
    }

    public void resetSpeed() {
        resetMultiplier();
    }

    public void setDelays(int delay) {
        setPressDelay(delay);
        setReleaseDelay(delay);
    }

    public void setMinDelay(int delay) {
        this.minDelay = delay;
    }

    public void setMultiplier(float multiplier) {
        if (multiplier < 0) {
            this.multiplier = 0;
        } else {
            this.multiplier = multiplier;
        }
    }

    public void setPressDelay(int pressDelay) {
        if (pressDelay < 0) {
            this.pressDelay = 0;
        } else {
            this.pressDelay = pressDelay;
        }
    }

    public void setReleaseDelay(int releaseDelay) {
        if (releaseDelay < 0) {
            this.releaseDelay = 0;
        } else {
            this.releaseDelay = releaseDelay;
        }
    }

    public void setSpeed(float multiplier) {
        setMultiplier(1f / multiplier);
    }

    public void type(String keys) {
        String[] keyList = keys.split(" ");
        for (String key : keyList) {
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

    /// private

    public int getMultipliedPressDelay() {
        return checkDelay((int) (pressDelay * multiplier));
    }

    public int getMultipliedReleaseDelay() {
        return checkDelay((int) (releaseDelay * multiplier));
    }

    private int checkDelay(int delay) {
        if (delay < minDelay) return minDelay;

        return delay;
    }

}
