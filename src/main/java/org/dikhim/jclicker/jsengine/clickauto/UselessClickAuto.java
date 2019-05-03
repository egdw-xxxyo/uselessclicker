package org.dikhim.jclicker.jsengine.clickauto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.jsengine.robot.RobotFactory;
import org.dikhim.jclicker.jsengine.clickauto.objects.*;

import java.awt.*;

public class UselessClickAuto extends ClickAuto {
    private BooleanProperty isRunning = new SimpleBooleanProperty(false);
    ClickAuto clickAuto;
    private Robot robot;

    public UselessClickAuto() throws AWTException {
        super();
        RobotFactory.createEmptyInstanceOnFail(true);
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

    public boolean isIsRunning() {
        return isRunning.get();
    }

    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning.set(isRunning);
    }
}
