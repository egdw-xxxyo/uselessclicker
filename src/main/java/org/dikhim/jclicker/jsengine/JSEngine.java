package org.dikhim.jclicker.jsengine;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.jsengine.objects.*;
import org.dikhim.jclicker.util.Out;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by dikobraz on 26.03.17.
 */
public class JSEngine {
    private BooleanProperty running = new SimpleBooleanProperty(false);

    private ScheduledExecutorService service;
    private List<Runnable> tasks = new ArrayList<>();

    private Robot robot;
    private ScriptEngine engine;
    private Thread thread;
    private String code;

    public JSEngine(Robot robot) {
        this.robot = robot;
    }

    public Robot getRobot() {
        return robot;
    }

    public void start() throws ScriptException {
        stop();
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(() -> {
            if (thread == null) thread = Thread.currentThread();
            scheduledTask();
        }, 0, 17, TimeUnit.MILLISECONDS);

        addTask(() -> {
            KeyEventsManager.getInstance().removeListenersByPrefix("script.");
            engine = new ScriptEngineManager().getEngineByName("nashorn");
            MouseObject mouseObject = new JsMouseObject(robot);
            KeyboardObject keyboardObject = new JsKeyboardObject(robot);
            SystemObject systemObject = new JsSystemObject(this);

            engine.put("mouse", mouseObject);
            engine.put("key", keyboardObject);
            engine.put("system", systemObject);
            engine.put("combined", new JsCombinedObject(mouseObject, keyboardObject, systemObject));
            engine.put("clipboard", new JsClipboardObject());
            try {
                engine.eval(code);
            } catch (ScriptException e) {
                Out.println(e.getMessage());
                stop();
            }
        });
        Platform.runLater(() -> running.setValue(true));
    }

    public void putCode(String code) {
        this.code = code;
    }

    /**
     * Выполняется каждые н секунд в отедльном потоке
     * Проверяет список задач, выполняет и удаляет задачу.
     */

    private void scheduledTask() {
        @SuppressWarnings("rawtypes")
        Iterator it = tasks.iterator();
        while (it.hasNext()) {
            Runnable task = (Runnable) it.next();
            task.run();
            it.remove();
        }
    }


    /**
     * Stops the engine
     */
    @SuppressWarnings("deprecation")
    public void stop() {
        tasks.clear();
        if (service != null) {
            service.shutdown();
            try {
                service.awaitTermination(20, TimeUnit.MILLISECONDS);
                service.shutdownNow();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!service.isTerminated() && thread != null) {
                try {
                    thread.stop();
                } catch (ThreadDeath death) {

                }
                service = null;
            }
        }
        thread = null;
        Platform.runLater(() -> running.setValue(false));
    }

    /**
     * Добавляет задание для выполнения потоком движка
     *
     * @param task
     */
    public void addTask(Runnable task) {
        tasks.add(task);
    }

    /**
     * Вызывает функцию
     *
     * @param name
     * @param args
     */
    public void invokeFunction(String name, Object... args) {
        try {
            ((Invocable) engine).invokeFunction(name, args);
        } catch (ScriptException | NoSuchMethodException e) {
            Out.println(e.getMessage());
        }
    }

    public boolean isRunning() {
        return running.get();
    }

    public BooleanProperty runningProperty() {
        return running;
    }
}
