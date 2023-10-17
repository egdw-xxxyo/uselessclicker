package org.dikhim.clickauto.jsengine;

import org.dikhim.clickauto.jsengine.objects.ObjectContainer;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.util.logger.ClickAutoLog;

import javax.script.*;
import java.util.ArrayList;
import java.util.List;

public class ClickAutoScriptEngine {

    private final Robot robot;
    private ScriptEngine engine;
    private ScriptContext context;
    private MethodInvoker methodInvoker;
    private Thread thread;
    private final static int TIME_TO_WAIT_INTERRUPTION = 1000;
    private int timeToWaitInterruption = TIME_TO_WAIT_INTERRUPTION;
    private volatile ObjectContainer objectContainer;

    public ClickAutoScriptEngine(Robot robot) {
        this.robot = robot;
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        methodInvoker = new MethodInvoker(engine);
        objectContainer = new ObjectContainer(this);
    }

    /**
     * Runs all scripts in now context
     */
    public void start() {
        waitAllThreadsEnd();
        runInNewThread(() ->
        {
            resetContext();
            objectContainer.getObjects().forEach((name, object) -> context.setAttribute(name, object, ScriptContext.ENGINE_SCOPE));
            try {
                for (String s : scripts) {
                    engine.eval(s, context);
                }
            } catch (Exception e) {
                ClickAutoLog.get().error(e.getMessage());
            }
        });
    }

    /**
     * Stops the engine
     */
    public void stop() {
        try {
            if (thread != null) {
                thread.interrupt();
                thread.join(timeToWaitInterruption);
                if (thread.isAlive()) {
                    try {
                        thread.stop();
                    } catch (ThreadDeath ignored) {
                    }
                }
                thread = null;
            }
                methodInvoker.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private List<String> scripts = new ArrayList<>();

    public void putScript(String script) {
        waitMainThreadEnd();
        scripts.add(script);
    }


    public void putObject(String name, Object object) {
        waitMainThreadEnd();
        objectContainer.put(name, object);
    }

    private void runInNewThread(Runnable runnable) {
        thread = new Thread(runnable);
        thread.start();
    }


    public void invokeFunction(String name, Object... args) {
        waitMainThreadEnd();
        methodInvoker.invokeMethod(name,args);
    }

    public void registerInvocableMethod(String name, int maxNumberOfThreads) {
        methodInvoker.registerMethod(name, maxNumberOfThreads);
    }

    public void removeScripts() {
        waitMainThreadEnd();
        scripts.clear();
    }

    public void removeObjects() {
        waitMainThreadEnd();
        objectContainer.resetToDefault();
    }


    /**
     * Full reset
     */
    public void reset() {
        stop();
        removeObjects();
        removeScripts();
        timeToWaitInterruption = TIME_TO_WAIT_INTERRUPTION;
    }

    /**
     * Reset context. All as it was before, but new one
     */

    private void resetContext() {
        context = new SimpleScriptContext();
        context.setBindings(engine.createBindings(), ScriptContext.GLOBAL_SCOPE);
        engine.setContext(context);
    }

    public int getTimeToWaitInterruption() {
        return timeToWaitInterruption;
    }

    public void setTimeToWaitInterruption(int timeToWaitInterruption) {
        this.timeToWaitInterruption = timeToWaitInterruption;
    }

    public Robot getRobot() {
        return this.robot;
    }

    public ObjectContainer getObjectContainer() {
        return objectContainer;
    }

    public void setObjectContainer(ObjectContainer objectContainer) {
        this.objectContainer = objectContainer;
    }

    public void waitMainThreadEnd() {
        try {
            while (thread != null && thread.isAlive()) {
                thread.join(100);
            }
        } catch (InterruptedException ignored) {
        }
    }

    public void waitAllThreadsEnd() {
        try {
            while (thread != null && thread.isAlive() ||methodInvoker.hasAliveThreads()) {
                Thread.sleep(100);
            }
        } catch (InterruptedException ignored) {
        }
    }
    
    
}
