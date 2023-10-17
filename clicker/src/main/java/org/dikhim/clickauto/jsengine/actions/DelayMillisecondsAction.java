package org.dikhim.clickauto.jsengine.actions;

public class DelayMillisecondsAction implements Action {
    private ActionType actionType = ActionType.DELAY_MILLISECONDS;

    private int delay;

    public DelayMillisecondsAction(int delay) {
        this.delay = delay;
    }

    public ActionType getType() {
        return actionType;
    }

    public int getDelay() {
        return delay;
    }
}
