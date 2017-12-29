package org.dikhim.jclicker.events;

import org.dikhim.jclicker.util.LimitedSizeQueue;

import java.util.ArrayList;
import java.util.List;

public class EventLogger {
    private LimitedSizeQueue<Event> eventLog;

    private LimitedSizeQueue<KeyboardEvent> keyboardLog;

    private LimitedSizeQueue<MouseButtonEvent> mouseButtonLog;
    private LimitedSizeQueue<MouseWheelEvent> mouseWheelLog;
    private LimitedSizeQueue<MouseMoveEvent> mouseMoveLog;

    public EventLogger(int n) {
        if (n < 2) throw new IllegalArgumentException("EventLogger size cannot be less than 2");
        eventLog = new LimitedSizeQueue<>(n);
        keyboardLog = new LimitedSizeQueue<>(n);
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
        mouseButtonLog.add(event);
    }

    public void add(MouseWheelEvent event) {
        eventLog.add(event);
        mouseWheelLog.add(event);
    }

    public void add(MouseMoveEvent event) {
        eventLog.add(event);
        mouseMoveLog.add(event);
    }

    public void clear() {
        eventLog.clear();
        keyboardLog.clear();
        mouseButtonLog.clear();
        mouseWheelLog.clear();
        mouseMoveLog.clear();
    }

    public long getDelay() {
        if (eventLog.size() < 2) return -1;
        return eventLog.getFromEnd(0).getTime() - eventLog.getFromEnd(1).getTime();
    }


}
