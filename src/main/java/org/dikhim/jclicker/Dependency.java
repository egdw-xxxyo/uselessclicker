package org.dikhim.jclicker;

import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.global.Mouse;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;

public class Dependency {
    private static volatile ClickAuto clickAuto;
    private static volatile ClickAutoScriptEngine engine;
    private static volatile Robot robot;
    private static final Mouse mouse;
    private static final MainConfiguration config;

    private static volatile EventManager eventManager;
    static {
        mouse = new Mouse();
        InputStream is = Dependency.class.getResourceAsStream("/config.json");
        JsonReader jsonReader = Json.createReader(is);
        JsonObject jsonObject = jsonReader.readObject();
        config = new MainConfiguration(jsonObject, "main");
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
    
    public static Mouse getMouse() {
        return mouse;
    }

    public static MainConfiguration getConfig() {
        return config;
    }
}
