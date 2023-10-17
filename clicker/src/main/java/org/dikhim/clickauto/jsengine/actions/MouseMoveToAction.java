package org.dikhim.clickauto.jsengine.actions;

public class MouseMoveToAction implements Action{
    private ActionType actionType = ActionType.MOUSE_MOVE_TO;
    private int x;
    private int y;

    public MouseMoveToAction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ActionType getType() {
        return actionType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
