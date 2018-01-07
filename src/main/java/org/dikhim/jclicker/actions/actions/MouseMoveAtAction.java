package org.dikhim.jclicker.actions.actions;

public class MouseMoveAtAction implements Action{
    private ActionType actionType = ActionType.MOUSE_MOVE_AT;
    private int x;
    private int y;

    public MouseMoveAtAction(int x, int y) {
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
