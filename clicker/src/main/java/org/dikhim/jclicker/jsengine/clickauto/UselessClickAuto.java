package org.dikhim.jclicker.jsengine.clickauto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.jsengine.robot.RobotFactory;
import org.dikhim.jclicker.jsengine.clickauto.objects.*;

import java.awt.*;

public class UselessClickAuto {
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private volatile ClickAuto clickAuto;
    private volatile Robot robot;

    public UselessClickAuto() {
        super();
        RobotFactory.createEmptyInstanceOnFail(true);
        try {
            clickAuto = new ClickAuto();
            robot = clickAuto.robot();
            ClipboardObject clipboardObject = new UselessClipboardObject(robot);
            MouseObject mouseObject = new UselessMouseObject(robot);
            KeyboardObject keyboardObject = new UselessKeyboardObject(robot);
            SystemObject systemObject = new UselessSystemObject(clickAuto.getEngine());
            CombinedObject combinedObject = new UselessCombinedObject(mouseObject, keyboardObject, systemObject);
            CreateObject createObject = new UselessCreateObject();
            ScreenObject screenObject = new UselessScreenObject(robot);


            clickAuto.objectContainer().put("clipboard", clipboardObject);
            clickAuto.objectContainer().put("combined", combinedObject);
            clickAuto.objectContainer().put("create", createObject);
            clickAuto.objectContainer().put("key", keyboardObject);
            clickAuto.objectContainer().put("mouse", mouseObject);
            clickAuto.objectContainer().put("screen", screenObject);
            clickAuto.objectContainer().put("system", systemObject);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public void putScript(String script) {
        clickAuto.putScript(script);
    }

    public void start() {
        clickAuto.start();
        isRunning.setValue(true);
    }

    public void stop() {
        clickAuto.stop();
        isRunning.setValue(false);
    }

    public void removeScripts() {
        clickAuto.removeScripts();
    }

    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public boolean isRunning() {
        return isRunningProperty().get();
    }

    public Robot robot() {
        return robot;
    }

    public ClickAutoScriptEngine getEngine() {
        return clickAuto.getEngine();
    }
}
