package org.dikhim.jclicker.actions.actions;

public class MousePressRightAction implements Action {
    private ActionType actionType = ActionType.MOUSE_PRESS_RIGHT;

    public MousePressRightAction() {
    }

    public ActionType getType() {
        return actionType;
    }
}
