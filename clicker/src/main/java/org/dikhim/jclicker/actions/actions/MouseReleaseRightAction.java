package org.dikhim.jclicker.actions.actions;

public class MouseReleaseRightAction implements Action{
    private ActionType actionType = ActionType.MOUSE_RELEASE_RIGHT;

    public MouseReleaseRightAction() {
    }

    public ActionType getType() {
        return actionType;
    }
}
