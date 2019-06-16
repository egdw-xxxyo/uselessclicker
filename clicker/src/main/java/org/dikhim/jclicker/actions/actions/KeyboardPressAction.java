package org.dikhim.jclicker.actions.actions;

public class KeyboardPressAction implements Action {
    private ActionType actionType = ActionType.KEYBOARD_PRESS;
    private String key;

    public KeyboardPressAction(String key) {
        this.key = key;
    }

    public ActionType getType() {
        return actionType;
    }

    public String getKey() {
        return key;
    }
}
