package org.dikhim.clickauto.jsengine.actions;

public class MousePressLeftAction implements Action {
    private ActionType actionType = ActionType.MOUSE_PRESS_LEFT;

    public MousePressLeftAction() {
    }

    public ActionType getType() {
        return actionType;
    }
}
