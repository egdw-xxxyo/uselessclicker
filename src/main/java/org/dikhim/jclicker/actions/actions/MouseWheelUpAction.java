package org.dikhim.jclicker.actions.actions;

public class MouseWheelUpAction implements Action {
    private ActionType actionType = ActionType.MOUSE_WHEEL_UP;
    private int amount;

    public MouseWheelUpAction(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public ActionType getType() {
        return actionType;
    }
}
