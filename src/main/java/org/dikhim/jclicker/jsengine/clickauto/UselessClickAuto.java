package org.dikhim.jclicker.jsengine.clickauto;

import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.clickauto.jsengine.robot.RobotFactory;
import org.dikhim.jclicker.jsengine.clickauto.objects.*;

import java.awt.*;

public class UselessClickAuto {
    ClickAuto clickAuto;
    private Robot robot;

    public UselessClickAuto() {
        try {
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

        } catch (AWTException ignore) {
        }

    }
}
