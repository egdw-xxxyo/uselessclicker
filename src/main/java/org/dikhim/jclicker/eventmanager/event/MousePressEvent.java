package org.dikhim.jclicker.eventmanager.event;

public class MousePressEvent implements MouseButtonEvent {
    private final String button;
    private final int x;
    private final int y;
    private final long time;


    public MousePressEvent(String button, int x, int y, long time) {
        this.button = button;
        this.x = x;
        this.y = y;
        this.time = time;
    }

    @Override
    public String getButton() {
        return button;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }


    @Override
    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "MousePressEvent{" +
                "button='" + button + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", time=" + time +
                '}';
    }
}
