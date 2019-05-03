package org.dikhim.jclicker;

import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.robot.Robot;

public class Dependecy {
    private static ClickAuto clickAuto;
    private static ClickAutoScriptEngine engine;
    private static Robot robot;

    public static void setClickAuto(ClickAuto clickAuto) {
        Dependecy.clickAuto = clickAuto;
        engine = clickAuto.getEngine();
        robot = clickAuto.robot();
    }

    public static ClickAuto getClickAuto() {
        return clickAuto;
    }

    public static ClickAutoScriptEngine getEngine() {
        return engine;
    }

    public static Robot getRobot() {
        return robot;
    }
}
