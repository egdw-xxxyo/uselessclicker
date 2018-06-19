package org.dikhim.jclicker.jsengine.objects;

import javafx.application.Platform;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.ShortcutEqualsListener;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.jsengine.robot.Robot;
import org.dikhim.jclicker.util.Out;


@SuppressWarnings("unused")
public class JsSystemObject implements SystemObject {
    private JSEngine engine;
    private Robot robot;
    private final Object monitor;

    private final float MULTIPLIER = 1;

    private float multiplier = MULTIPLIER;

    public JsSystemObject(JSEngine engine) {
        this.engine = engine;
        this.robot = engine.getRobot();
        this.monitor = robot.getMonitor();
    }

    public JsSystemObject(Robot robot) {
        this.robot = robot;
        this.monitor = robot.getMonitor();
    }

    @Override
    public void exit() {
        Platform.exit();
    }

    @Override
    public int getMultipliedDelay(int ms) {
        synchronized (monitor) {
            int result = ((int) (ms * multiplier));
            if (result <= 0) {
                return 0;
            } else {
                return result;
            }
        }
    }

    @Override
    public float getMultiplier() {
        synchronized (monitor) {
            return multiplier;
        }
    }

    @Override
    public float getSpeed() {
        synchronized (monitor) {
            return 1f / multiplier;
        }
    }

    @Override
    public void onKeyPress(String functionName, Object... args) {
        //TODO
    }

    @Override
    public void onKeyRelease(String functionName, Object... args) {
        //TODO

    }

    @Override
    public void onMousePress(String functionName, Object... args) {
        //TODO

    }

    @Override
    public void onMouseRelease(String functionName, Object... args) {
        //TODO

    }

    @Override
    public void onMouseMove(String functionName, Object... args) {
        //TODO

    }

    @Override
    public void onWheelDown(String functionName, Object... args) {
        //TODO

    }

    @Override
    public void onWheelUp(String functionName, Object... args) {
        //TODO
    }

    @Override
    public void print(String s) {
        synchronized (monitor) {
            Out.print(s);
        }
    }

    @Override
    public void println() {
        synchronized (monitor) {
            Out.println("");
        }
    }

    @Override
    public void println(String s) {
        synchronized (monitor) {
            Out.println(s);
        }
    }

    /**
     * Register shortcut for call function
     *
     * @param function name of function
     * @param shortcut list of names of keys
     */
    @Override
    public void registerShortcut(String shortcut, String function) {
        synchronized (monitor) {
            ShortcutEqualsListener handler = new ShortcutEqualsListener("script." + function,
                    shortcut, "PRESS", (e) -> engine.invokeFunction(function));
            KeyEventsManager.getInstance().addKeyboardListener(handler);
        }
    }

    @Override
    public void resetMultiplier() {
        synchronized (monitor) {
            this.multiplier = MULTIPLIER;
        }
    }

    @Override
    public void resetSpeed() {
        synchronized (monitor) {
            this.multiplier = MULTIPLIER;
        }
    }

    @Override
    public void setMultiplier(float multiplier) {
        synchronized (monitor) {
            this.multiplier = multiplier;
        }
    }

    @Override
    public void sleep(int ms) {
        if (ms <= 0) return;
        try {
            Thread.sleep(getMultipliedDelay(ms));
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void setSpeed(float multiplier) {
        synchronized (monitor) {
            this.multiplier = 1f / multiplier;
        }
    }
}
