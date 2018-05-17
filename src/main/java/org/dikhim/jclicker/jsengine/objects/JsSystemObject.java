package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.ShortcutEqualsListener;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.util.Out;

import java.awt.*;

@SuppressWarnings("unused")
public class JsSystemObject implements SystemObject {
    private JSEngine engine;
    private Robot robot;

    private final float MULTIPLIER = 1;

    private float multiplier = MULTIPLIER;

    public JsSystemObject(JSEngine engine) {
        this.engine = engine;
        this.robot = engine.getRobot();
    }

    public JsSystemObject(Robot robot) {
        this.robot = robot;
    }

    public int getMultipliedDelay(int ms) {
        int result = ((int) (ms * multiplier));
        if (result <= 0) {
            return 0;
        } else {
            return result;
        }
    }

    public float getMultiplier() {
        return multiplier;
    }

    public float getSpeed() {
        return 1f / multiplier;
    }

    public void print(String s) {
        Out.print(s);
    }

    public void println(String s) {
        Out.println(s);
    }

    /**
     * Register shortcut for call function
     *
     * @param function name of function
     * @param shortcut list of names of keys
     */

    public void registerShortcut(String shortcut, String function) {
        ShortcutEqualsListener handler = new ShortcutEqualsListener("script." + function,
                shortcut, "PRESS", (e) -> engine.addTask(() -> engine.invokeFunction(function)));
        KeyEventsManager.getInstance().addKeyboardListener(handler);
    }

    public void resetMultiplier() {
        this.multiplier = MULTIPLIER;
    }


    public void resetSpeed() {
        this.multiplier = MULTIPLIER;
    }

    public void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    public void sleep(int ms) {
        if (ms <= 0) return;
        try {
            Thread.sleep(getMultipliedDelay(ms));
        } catch (InterruptedException e) {
        }
    }

    public void sleepNonMultiplied(int ms) {
        if (ms <= 0) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public void setSpeed(float multiplier) {
        this.multiplier = 1f / multiplier;
    }
}
