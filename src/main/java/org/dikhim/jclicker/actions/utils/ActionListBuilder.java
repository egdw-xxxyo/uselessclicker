package org.dikhim.jclicker.actions.utils;

import org.dikhim.jclicker.actions.actions.Action;
import org.dikhim.jclicker.actions.actions.KeyboardPressAction;
import org.dikhim.jclicker.actions.actions.KeyboardReleaseAction;

import java.util.ArrayList;
import java.util.List;

public class ActionListBuilder {
    private List<Action> actions = new ArrayList<>();
    
    public void addKeyPress(String key) {
        actions.add(new KeyboardPressAction(key));
    }

    public void addKeyRelease(String key) {
        actions.add(new KeyboardReleaseAction(key));
    }

    public void addKeyType(String key) {
        addKeyPress(key);
        addKeyRelease(key);
    }

    public List<Action> getActions() {
        return actions;
    }
}
