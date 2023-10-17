package org.dikhim.clickauto.jsengine.utils;


import org.dikhim.clickauto.jsengine.events.*;
import org.dikhim.clickauto.util.LimitedSizeQueue;

public class EventLogger {
    private LimitedSizeQueue<Event> eventLog;

    private LimitedSizeQueue<KeyboardEvent> keyboardLog;

    private LimitedSizeQueue<MouseEvent> mouseLog;

    private LimitedSizeQueue<MouseButtonEvent> mouseButtonLog;
    private LimitedSizeQueue<MouseWheelEvent> mouseWheelLog;
    private LimitedSizeQueue<MouseMoveEvent> mouseMoveLog;

    public LimitedSizeQueue<Event> getEventLog() {
        return eventLog;
    }

    public EventLogger(int n) {
        if (n < 2) throw new IllegalArgumentException("EventLogger size cannot be less than 2");
        eventLog = new LimitedSizeQueue<>(n);
        keyboardLog = new LimitedSizeQueue<>(n);
        mouseLog = new LimitedSizeQueue<>(n);
        mouseButtonLog = new LimitedSizeQueue<>(n);
        mouseWheelLog = new LimitedSizeQueue<>(n);
        mouseMoveLog = new LimitedSizeQueue<>(n);
    }

    public void add(KeyboardEvent event) {
        eventLog.add(event);
        keyboardLog.add(event);
    }

    public void add(MouseButtonEvent event) {
        eventLog.add(event);
        mouseLog.add(event);
        mouseButtonLog.add(event);
    }

    public void add(MouseWheelEvent event) {
        eventLog.add(event);
        mouseLog.add(event);
        mouseWheelLog.add(event);
    }

    public void add(MouseMoveEvent event) {
        eventLog.add(event);
        mouseLog.add(event);
        mouseMoveLog.add(event);
    }

    public void clear() {
        eventLog.clear();
        keyboardLog.clear();
        mouseButtonLog.clear();
        mouseWheelLog.clear();
        mouseMoveLog.clear();
    }

    public int getDelay() {
        if (eventLog.size() < 2) return 0;
        return (int)(eventLog.getFromEnd(0).getTime() - eventLog.getFromEnd(1).getTime());
    }

    public Event getEventFromEnd(int index) {
        return eventLog.getFromEnd(index);
    }

    public MouseMoveEvent getMoveEventFromEnd(int index) {
        return mouseMoveLog.getFromEnd(index);
    }

    public MouseEvent getLastMouseEvent() {
        return mouseLog.getLast();
    }

    public MouseButtonEvent getLastMouseButtonEvent() {
        return mouseButtonLog.getLast();
    }

    public boolean isEmpty() {
        return eventLog.isEmpty();
    }
    public LimitedSizeQueue<MouseMoveEvent> getMouseMoveLog() {
        return mouseMoveLog;
    }

    public int size(){
        return eventLog.size();
    }

    public int getMouseButtonLogSize() {
        return mouseButtonLog.size();
    }

    // Mouse Event methods
    public int getMouseEventDx() {
        return mouseLog.getFromEnd(0).getX()-mouseLog.getFromEnd(1).getX();
    }
    public int getMouseEventDy() {
        return mouseLog.getFromEnd(0).getY()-mouseLog.getFromEnd(1).getY();
    }
}
