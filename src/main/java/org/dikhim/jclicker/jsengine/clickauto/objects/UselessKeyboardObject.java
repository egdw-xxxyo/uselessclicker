package org.dikhim.jclicker.jsengine.clickauto.objects;

import org.dikhim.clickauto.jsengine.objects.ScriptKeyboardObject;
import org.dikhim.clickauto.jsengine.robot.Robot;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;

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
            return KeyEventsManager.getInstance().isPressed(keySet);
        }
    }

    @Override
    public boolean isCapsLocked() {
        return KeyEventsManager.getInstance().isCapsLockLocked();
    }

    @Override
    public boolean isNumLocked() {
        return KeyEventsManager.getInstance().isNumLockLocked();
    }

    @Override
    public boolean isScrollLocked() {
        return KeyEventsManager.getInstance().isScrollLockLocked();
    }
}
