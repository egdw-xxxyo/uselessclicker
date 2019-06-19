package org.dikhim.jclicker.actions.actions;

public class MouseReleaseLeftAction implements Action {
    private ActionType actionType = ActionType.MOUSE_RELEASE_LEFT;

    public MouseReleaseLeftAction() {
    }

    public ActionType getType() {
        return actionType;
    }
}
