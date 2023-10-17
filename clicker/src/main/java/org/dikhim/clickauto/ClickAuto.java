package org.dikhim.clickauto;

import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.objects.ObjectContainer;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.jsengine.robot.RobotFactory;
import org.dikhim.clickauto.util.logger.ClickAutoLog;

import java.awt.*;

public class ClickAuto {
    private ClickAutoScriptEngine engine;

    public ClickAuto() throws AWTException {
        engine = new ClickAutoScriptEngine(RobotFactory.get());
    }

    /**
     * Starts script engine. Any scripts that was putScript will be evaluated in new thread
     */
    public void start() {
        engine.start();
    }

    /**
     * Stops script engine. Sends interrupt request to all threads that life in engine.<br>
     * Status of thread may be checked by thread.interrupted() method<br>
     * The interrupt request should be proceed by script to do all necessary operations to complete the script.<br>
     * If after interrupt request thread is still alive, than it will be destroyed<br>
     * Destroying thread may cause crash for the script engine. Especially when it called right after the start() method. Better wait at least 500ms after start() method to call stop()
     */
    public void stop() {
        engine.stop();
    }

    /**
     * Put script into script engine. Script will be evaluated after start() method call
     *
     * @param script script to be evaluated
     */
    public void putScript(String script) {
        engine.putScript(script);
    }
    

    /**
     * Returns a container of script objects 
     *
     * @return a container for script objects
     */
    public ObjectContainer objectContainer() {
        return engine.getObjectContainer();
    }

    /**
     * Sets the object container
     * @param objectContainer the instance of ObjectContainer
     */
    public void setObjectContainer(ObjectContainer objectContainer) {
        engine.setObjectContainer(objectContainer);
    }

    /**
     * Resets the script engine to default configuration<br>
     * Performs sequential calls for
     * <ul>
     * <li>stop the engine</li>
     * <li>reset script objects to default realizations (mouse, keyboard, system, clipboard, combined, thread, create)</li>
     * <li>remove all inserted scripts</li>
     * <li>reset time to wait interruption to 1000 ms</li>
     * </ul>
     */
    public void reset() {
        engine.reset();
    }


    /**
     * Removes all user scripts
     */
    public void removeScripts() {
        engine.removeScripts();
    }

    /**
     * Removes all user objects
     */
    public void removeObjects() {
        engine.removeObjects();
    }
    

    /**
     * Invokes a function by name
     *
     * @param name - function name that should be called
     * @param ars  - function parameters
     */
    public void callFunction(String name, Object... ars) {
        engine.invokeFunction(name, ars);
    }

    /**
     * When stop method is called it sends an interruption request to threads and waits specified amount of time. After that time threads will be destroyed
     *
     * @param ms time to wait interruption in milliseconds
     */
    public void setTimeToWaitInterruption(int ms) {
        engine.setTimeToWaitInterruption(ms);
    }

    /**
     * When stop method is called it sends an interruption request to threads and waits specified amount of time. After that time threads will be destroyed
     *
     * @return time to wait interruption in milliseconds
     */
    public int getTimeToWaitInterruption() {
        return engine.getTimeToWaitInterruption();
    }

    /**
     * Returns an instance of the Robot object<br>
     * Use it for your own script objects
     *
     * @return robot object
     */
    public Robot robot() {
        return engine.getRobot();
    }

    /**
     * Returns an instance of the ClickAutoScriptEngine object<br>
     * Ii's needed for creation some specific objects like system object
     *
     * @return engine object
     */
    public ClickAutoScriptEngine getEngine() {
        return engine;
    }

    /**
     * Wait until evaluation is complete.
     */
    public void waitEvaluationComplete() {
        engine.waitMainThreadEnd();
    }

    /**
     * Wait until at least one thread is alive
     */
    public void waitAllThreadsEnd() {
        engine.waitAllThreadsEnd();
    }

    /**
     * Get the logger
     * @return logger
     */
    public static ClickAutoLog getLogger() {
        return ClickAutoLog.get();
    }
}
