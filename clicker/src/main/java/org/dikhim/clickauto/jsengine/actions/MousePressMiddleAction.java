package org.dikhim.clickauto.jsengine.actions;

public class MousePressMiddleAction implements Action {
    private ActionType actionType = ActionType.MOUSE_PRESS_MIDDLE;

    public MousePressMiddleAction() {
    }

    public ActionType getType() {
        return actionType;
    }
}
