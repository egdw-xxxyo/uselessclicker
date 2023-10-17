package org.dikhim.clickauto.jsengine.actions;

public class MouseMoveAction implements Action {
    private ActionType actionType = ActionType.MOUSE_MOVE;
    private int dx;
    private int dy;

    public MouseMoveAction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public ActionType getType() {
        return actionType;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
