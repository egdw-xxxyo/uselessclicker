package org.dikhim.clickauto.jsengine.events;

import java.util.Set;

public class MouseButtonEvent implements MouseEvent {
    private int x;
    private int y;
    private long time;
    private String button;
    private Set<String> pressed;
    private String action;

    public Set<String> getPressed() {
        return pressed;
    }

    public void setPressed(Set<String> pressed) {
        this.pressed = pressed;
    }

    public MouseButtonEvent(String button, Set<String> pressed, String action, int x, int y, long time) {
        this.button = button;
        this.pressed = pressed;
        this.action = action;
        this.x = x;
        this.y = y;
        this.time = time;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the button
     */
    public String getButton() {
        return button;
    }

    /**
     * @param button the button to set
     */
    public void setButton(String button) {
        this.button = button;
    }

    @Override
    public EventType getType() {
        return EventType.MOUSE_BUTTON;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
