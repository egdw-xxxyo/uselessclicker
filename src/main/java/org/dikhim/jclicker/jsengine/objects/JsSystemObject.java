package org.dikhim.jclicker.jsengine.objects;

import javafx.application.Platform;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.jsengine.robot.Robot;
import org.dikhim.jclicker.util.MathUtil;
import org.dikhim.jclicker.util.Out;

import java.awt.image.BufferedImage;


@SuppressWarnings("unused")
public class JsSystemObject implements SystemObject {
    private JSEngine engine;
    private Robot robot;
    private final Object monitor;

    private final double MULTIPLIER = 1;

    private double multiplier = MULTIPLIER;

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
    public double getMultiplier() {
        synchronized (monitor) {
            return multiplier;
        }
    }

    @Override
    public double getSpeed() {
        synchronized (monitor) {
            if(multiplier==0) return 999999999;
            return MathUtil.roundTo1(1.0 / multiplier);
        }
    }

    @Override
    public void keyIgnore() {
        KeyEventsManager.getInstance().ignorePrefix("script.");
    }

    @Override
    public void keyResume() {
        KeyEventsManager.getInstance().removeIgnorePrefix("script.");
    }

    @Override
    public void mouseIgnore() {
        MouseEventsManager.getInstance().ignorePrefix("script.");
    }

    @Override
    public void mouseResume() {
        MouseEventsManager.getInstance().removeIgnorePrefix("script.");
    }

    @Override
    public void onKeyPress(String functionName, String key, Object... args) {
        KeyboardListener listener = new KeyListener(
                "script." + functionName + "." + key + ".press",
                key,
                "PRESS",
                e -> engine.invokeFunction(functionName, args));
        KeyEventsManager.getInstance().addKeyboardListener(listener);
    }

    @Override
    public void onKeyRelease(String functionName, String key, Object... args) {
        KeyboardListener listener = new KeyListener(
                "script." + functionName + "." + key + ".release",
                key,
                "RELEASE",
                (e) -> engine.invokeFunction(functionName, args));
        KeyEventsManager.getInstance().addKeyboardListener(listener);
    }

    @Override
    public void onShortcutPress(String functionName, String keys, Object... args) {
        KeyboardListener listener = new ShortcutEqualsListener(
                "script." + functionName + "." + keys + ".press",
                keys,
                "PRESS",
                (e) -> engine.invokeFunction(functionName, args)
        );
        KeyEventsManager.getInstance().addKeyboardListener(listener);
    }

    @Override
    public void onShortcutRelease(String functionName, String keys, Object... args) {
        KeyboardListener listener = new ShortcutEqualsListener(
                "script." + functionName + "." + keys + ".release",
                keys,
                "RELEASE",
                (e) -> engine.invokeFunction(functionName, args)
        );
        KeyEventsManager.getInstance().addKeyboardListener(listener);
    }

    @Override
    public void onMousePress(String functionName, String buttons, Object... args) {
        MouseButtonHandler mouseButtonHandler = new MouseButtonHandler(
                "script." + functionName + "." + buttons + ".press",
                buttons,
                "PRESS",
                e -> engine.invokeFunction(functionName, args));
        MouseEventsManager.getInstance().addButtonListener(mouseButtonHandler);
    }

    @Override
    public void onMouseRelease(String functionName, String buttons, Object... args) {
        MouseButtonHandler mouseButtonHandler = new MouseButtonHandler(
                "script." + functionName + "." + buttons + ".release",
                buttons,
                "RELEASE",
                e -> engine.invokeFunction(functionName, args));
        MouseEventsManager.getInstance().addButtonListener(mouseButtonHandler);
    }

    @Override
    public void onMouseMove(String functionName, Object... args) {
        MouseMoveHandler mouseMoveHandler = new MouseMoveHandler(
                "script." + functionName + ".move",
                e -> engine.invokeFunction(functionName, args));
        MouseEventsManager.getInstance().addMoveListener(mouseMoveHandler);
    }

    @Override
    public void onWheelDown(String functionName, Object... args) {
        MouseWheelHandler mouseWheelHandler = new MouseWheelHandler(
                "script." + functionName + ".wheel.down",
                "DOWN",
                e -> engine.invokeFunction(functionName, args));
        MouseEventsManager.getInstance().addWheelListener(mouseWheelHandler);
    }

    @Override
    public void onWheelUp(String functionName, Object... args) {
        MouseWheelHandler mouseWheelHandler = new MouseWheelHandler(
                "script." + functionName + ".wheel.up",
                "UP",
                e -> engine.invokeFunction(functionName, args));
        MouseEventsManager.getInstance().addWheelListener(mouseWheelHandler);
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

    @Override
    public void setMaxThreads(String name, int maxThreads) {
        engine.registerInvocableMethod(name, maxThreads);
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
    public void setMultiplier(double multiplier) {
        synchronized (monitor) {
            if (multiplier < 0) {
                this.multiplier = 0;
            } else {
                this.multiplier = multiplier;
            }
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
    public void setSpeed(double speed) {
        synchronized (monitor) {
            if (speed < 0.1) {
                speed = 0.1;
            }
            speed = MathUtil.roundTo1(speed);
            setMultiplier(1f / speed);
        }
    }

    @Override
    public void showImage(Image image) {
        synchronized (monitor) {
            Platform.runLater(() -> Clicker.getApplication().getMainApplication().getOnSetOutputImage().accept(image));
        }
    }
}
