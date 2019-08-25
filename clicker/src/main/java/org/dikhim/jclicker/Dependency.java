package org.dikhim.jclicker;

import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.jclicker.configuration.Configuration;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.jsengine.clickauto.UselessClickAuto;

public class Dependency {
    private final static UselessClickAuto clickAuto;
    private final static Robot robot;
    private final static Configuration configuration;

    private static volatile EventManager eventManager;

    static {
        clickAuto = new UselessClickAuto();
        robot = clickAuto.robot();
        configuration = new Configuration();
    }

    public static UselessClickAuto getClickAuto() {
        return clickAuto;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static void setEventManager(EventManager eventManager) {
        Dependency.eventManager = eventManager;
    }

    public static Robot getRobot() {
        return robot;
    }

    public static Configuration getConfig() {
        return configuration;
    }
}
