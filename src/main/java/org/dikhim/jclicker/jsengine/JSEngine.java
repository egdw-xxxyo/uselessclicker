package org.dikhim.jclicker.jsengine;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.jsengine.objects.*;
import org.dikhim.jclicker.jsengine.robot.Robot;
import org.dikhim.jclicker.util.Out;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by dikobraz on 26.03.17.
 */
public class JSEngine {
    private BooleanProperty running = new SimpleBooleanProperty(false);

    private final Robot robot;
    private ScriptEngine engine;
    private Thread thread;
    private String code;

    private MethodInvoker methodInvoker;


    public JSEngine(Robot robot) {
        this.robot = robot;
    }

    public Robot getRobot() {
        return robot;
    }

    public void start() {
        stop();
        thread = new Thread(() -> {
            engine = new ScriptEngineManager().getEngineByName("nashorn");
            methodInvoker = new MethodInvoker(engine);
            KeyboardObject keyboardObject = new JsKeyboardObject(robot);
            MouseObject mouseObject = new JsMouseObject(robot);
            SystemObject systemObject = new JsSystemObject(this);
            CombinedObject combinedObject = new JsCombinedObject(mouseObject, keyboardObject, systemObject);
            ClipboardObject clipboardObject = new JsClipboardObject(robot);

            engine.put("mouse", mouseObject);
            engine.put("key", keyboardObject);
            engine.put("system", systemObject);
            engine.put("combined", combinedObject);
            engine.put("clipboard", clipboardObject);
            try {
                engine.eval(code);
            } catch (ScriptException e) {
                Out.println(e.getMessage());
                stop();
            }
        });
        thread.start();
        Platform.runLater(() -> running.setValue(true));
    }

    public void putCode(String code) {
        this.code = code;
    }

    /**
     * Stops the engine
     */
    @SuppressWarnings("deprecation")
    public void stop() {
        KeyEventsManager.getInstance().removeListenersByPrefix("script.");
        MouseEventsManager.getInstance().removeListenersByPrefix("script.");
        if (thread != null) {
            try {
                thread.stop();
            } catch (ThreadDeath ignored) {

            }
            thread = null;
        }
        if (methodInvoker != null) {
            methodInvoker.stop();
            methodInvoker = null;
        }
        Platform.runLater(() -> running.setValue(false));
    }

    public void invokeFunction(String name, Object... args) {
        methodInvoker.invokeMethod(name, args);
    }

    public void registerInvocableMethod(String name, int maxNumberOfThreads) {
        methodInvoker.registerMethod(name, maxNumberOfThreads);
    }

    public boolean isRunning() {
        return running.get();
    }

    public BooleanProperty runningProperty() {
        return running;
    }
}
