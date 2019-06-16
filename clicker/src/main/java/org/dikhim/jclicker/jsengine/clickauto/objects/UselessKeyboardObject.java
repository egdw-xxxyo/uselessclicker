package org.dikhim.jclicker.jsengine.clickauto.objects;

import org.dikhim.clickauto.jsengine.objects.ScriptKeyboardObject;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.jclicker.Dependency;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UselessKeyboardObject extends ScriptKeyboardObject implements KeyboardObject {

    public UselessKeyboardObject(Robot robot) {
        super(robot);
    }

    @Override
    public boolean isPressed(String keys) {
        synchronized (robot) {
            Set<String> keySet = new HashSet<>(Arrays.asList(keys.split(" ")));
            return Dependency.getEventManager().getKeyboard().isPressed(keySet);
        }
    }

    @Override
    public boolean isCapsLocked() {
        return Dependency.getEventManager().getKeyboard().isCapsLockLocked();
    }

    @Override
    public boolean isNumLocked() {
        return Dependency.getEventManager().getKeyboard().isNumLockLocked();
    }

    @Override
    public boolean isScrollLocked() {
        return Dependency.getEventManager().getKeyboard().isScrollLockLocked();
    }
}
