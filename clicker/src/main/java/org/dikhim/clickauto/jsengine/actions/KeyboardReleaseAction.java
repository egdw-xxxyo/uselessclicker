package org.dikhim.clickauto.jsengine.actions;

public class KeyboardReleaseAction implements Action{
    private ActionType actionType = ActionType.KEYBOARD_RELEASE;
    private String key;

    public KeyboardReleaseAction(String key) {
        this.key = key;
    }

    public ActionType getType() {
        return actionType;
    }

    public String getKey() {
        return key;
    }
}
