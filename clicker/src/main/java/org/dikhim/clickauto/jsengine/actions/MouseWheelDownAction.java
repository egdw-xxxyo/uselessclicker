package org.dikhim.clickauto.jsengine.actions;

public class MouseWheelDownAction implements Action {
    private ActionType actionType = ActionType.MOUSE_WHEEL_DOWN;
    private int amount;

    public MouseWheelDownAction(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public ActionType getType() {
        return actionType;
    }
}
