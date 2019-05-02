package org.dikhim.jclicker.jsengine.clickauto.objects;

import javafx.application.Platform;
import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.objects.ScriptSystemObject;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.jsengine.objects.Classes.Image;

public class UselessSystemObject extends ScriptSystemObject implements SystemObject {
    public UselessSystemObject(ClickAutoScriptEngine engine) {
        super(engine);
    }

    @Override
    public void exit() {
        Platform.exit();
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
    public void setMaxThreads(String name, int maxThreads) {
        engine.registerInvocableMethod(name, maxThreads);
    }

    @Override
    public void showImage(Image image) {
        synchronized (robot) {
            Platform.runLater(() -> Clicker.getApplication().getMainApplication().getOnSetOutputImage().accept(image));
        }
    }
}
