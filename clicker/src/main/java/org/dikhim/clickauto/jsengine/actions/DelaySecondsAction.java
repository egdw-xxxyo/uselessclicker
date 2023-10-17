package org.dikhim.clickauto.jsengine.actions;

public class DelaySecondsAction implements Action{
    private ActionType actionType = ActionType.DELAY_SECONDS;

    private int delay;

    public DelaySecondsAction(int delay) {
        this.delay = delay;
    }

    public ActionType getType() {
        return actionType;
    }

    public int getDelay() {
        return delay;
    }
}
