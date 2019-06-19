package org.dikhim.jclicker.eventmanager.event;

public class MouseMoveEvent implements MouseMotionEvent {
    private final int x;
    private final int y;
    private final long time;

    public MouseMoveEvent(int x, int y, long time) {
        this.x = x;
        this.y = y;
        this.time = time;
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
        return "MouseMoveEvent{" +
                "x=" + x +
                ", y=" + y +
                ", time=" + time +
                '}';
    }
}
