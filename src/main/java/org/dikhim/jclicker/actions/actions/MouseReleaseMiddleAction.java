package org.dikhim.jclicker.actions.actions;

public class MouseReleaseMiddleAction implements Action {
    private ActionType actionType = ActionType.MOUSE_RELEASE_MIDDLE;

    public MouseReleaseMiddleAction() {
    }

    public ActionType getType() {
        return actionType;
    }
}
