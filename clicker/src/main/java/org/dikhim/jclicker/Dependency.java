package org.dikhim.jclicker;

import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.jclicker.configuration.Configuration;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.jsengine.clickauto.UselessClickAuto;

public class Dependency {
    private static volatile UselessClickAuto clickAuto;
    private static volatile Robot robot;
    private static final Configuration configuration;

    private static volatile EventManager eventManager;
    static {
        configuration = new Configuration();
    }
    public static void setClickAuto(UselessClickAuto clickAuto) {
        Dependency.clickAuto = clickAuto;
        robot = clickAuto.robot();
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
