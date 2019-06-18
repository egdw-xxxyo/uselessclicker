package org.dikhim.jclicker.jsengine.clickauto.objects;

import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.robot.RobotFactory;

import java.awt.*;

public class UselessClickAuto {
    private ClickAuto clickAuto;

    public UselessClickAuto() {
        RobotFactory.createEmptyInstanceOnFail(true);
        try {
            clickAuto = new ClickAuto();
        } catch (AWTException ignore) {
        }
        
    }
}
