package org.dikhim.jclicker;

import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.jclicker.configuration.Configuration;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.global.Keyboard;
import org.dikhim.jclicker.global.Mouse;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.util.Locale;

public class Dependency {
    private static volatile ClickAuto clickAuto;
    private static volatile ClickAutoScriptEngine engine;
    private static volatile Robot robot;
    private static final Configuration configuration;

    private static volatile EventManager eventManager;
    static {
        configuration = new Configuration();
    }
    public static void setClickAuto(ClickAuto clickAuto) {
        Dependency.clickAuto = clickAuto;
        engine = clickAuto.getEngine();
        robot = clickAuto.robot();
    }

    public static ClickAuto getClickAuto() {
        return clickAuto;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static void setEventManager(EventManager eventManager) {
        Dependency.eventManager = eventManager;
    }

    public static ClickAutoScriptEngine getEngine() {
        return engine;
    }

    public static Robot getRobot() {
        return robot;
    }

    public static Configuration getConfig() {
        return configuration;
    }
}
