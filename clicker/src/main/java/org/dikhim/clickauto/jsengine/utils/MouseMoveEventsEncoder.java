package org.dikhim.clickauto.jsengine.utils;


import org.dikhim.clickauto.jsengine.events.MouseMoveEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class MouseMoveEventsEncoder {
    private final int TOP_SHIFT = 10000;
    private final int BOTTOM_SHIFT = 5000;

    private int shift;
    private List<MouseMoveEvent> eventLog;

    private int maxNumber;
    private int minNumber;

    public MouseMoveEventsEncoder(int shift, List<MouseMoveEvent> eventLog) {
        this.shift = shift;
        this.eventLog = eventLog;
    }

    public List<MouseMoveEvent> getEventLog() {
        return eventLog;
    }

    public void setEventLog(List<MouseMoveEvent> eventLog) {
        this.eventLog = eventLog;
    }

    /**
     * Returns string representation of movement path.
     * Each char codes absolute coordinate.
     * Every 1st - x, 2nd - y.
     *
     * @return string representation of the movement path
     */
    public String getAbsolutePath() {
        return getAbsolutePath(eventLog);
    }

    private String getAbsolutePath(List<MouseMoveEvent> eventLog) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");
        for (MouseMoveEvent e : eventLog) {
            sb.append((char) (e.getX() + shift));
            sb.append((char) (e.getY() + shift));
        }
        return sb.toString();
    }

    /**
     * Returns string representation of movement path with delays.
     * Each char codes absolute coordinate and delay.
     * Every 1st - x, 2nd - y, 3rd - delay;
     *
     * @return string representation of the movement path
     */
    public String getAbsolutePathWithDelays() {
        return getAbsolutePathWithDelays(eventLog);
    }

    private String getAbsolutePathWithDelays(List<MouseMoveEvent> eventLog) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");
        long delay;

        for (int i = 0; i < eventLog.size(); i++) {
            sb.append((char) (eventLog.get(i).getX() + shift));
            sb.append((char) (eventLog.get(i).getY() + shift));

            if (i < eventLog.size() - 1) {
                delay = eventLog.get(i + 1).getTime() - eventLog.get(i).getTime();
            } else {
                delay = 0;
            }
            sb.append((char) (delay + shift));
        }
        return sb.toString();
    }

    public String getAbsolutePathFixRate(int fps) {
        return getAbsolutePathFixRateWithDelays(fps, eventLog);
    }

    private String getAbsolutePathFixRate(int fps, List<MouseMoveEvent> eventLog) {
        if (eventLog.size() < 2) return "";
        List<MouseMoveEvent> filteredEventLog = filterEventLogByFps(eventLog,fps);
        return getAbsolutePath(filteredEventLog);
    }

    public String getAbsolutePathFixRateWithDelays(int fps) {
        return getAbsolutePathFixRateWithDelays(fps, eventLog);
    }

    private String getAbsolutePathFixRateWithDelays(int fps, List<MouseMoveEvent> eventLog) {
        if (eventLog.size() < 2) return "";
        List<MouseMoveEvent> filteredEventLog = filterEventLogByFps(eventLog,fps);
        return getAbsolutePathWithDelays(filteredEventLog);
    }

    // relative
    public String getRelativePath() {
        return getRelativePath(eventLog);
    }

    private String getRelativePath(List<MouseMoveEvent> eventLog) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");
        int oldX = eventLog.get(0).getX();
        int oldY = eventLog.get(0).getY();
        int dx, dy;

        for (int i = 0; i < eventLog.size(); i++) {
            dx = eventLog.get(i).getX() - oldX;
            dy = eventLog.get(i).getY() - oldY;
            oldX = eventLog.get(i).getX();
            oldY = eventLog.get(i).getY();

            sb.append((char) (dx + shift));
            sb.append((char) (dy + shift));
        }
        return sb.toString();
    }

    public String getRelativePathWithDelays() {
        return getRelativePathWithDelays(eventLog);
    }

    private String getRelativePathWithDelays(List<MouseMoveEvent> eventLog) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");
        int oldX = eventLog.get(0).getX();
        int oldY = eventLog.get(0).getY();
        int dx, dy;
        long delay;

        for (int i = 1; i < eventLog.size(); i++) {
            dx = eventLog.get(i).getX() - oldX;
            dy = eventLog.get(i).getY() - oldY;
            oldX = eventLog.get(i).getX();
            oldY = eventLog.get(i).getY();
            sb.append((char) (dx + shift));
            sb.append((char) (dy + shift));

            if (i < eventLog.size() - 1) {
                delay = eventLog.get(i + 1).getTime() - eventLog.get(i).getTime();
            } else {
                delay = 0;
            }
            sb.append((char) (delay + shift));
        }
        return sb.toString();
    }

    public String getRelativePathFixRate(int fps) {
        return getRelativePathFixRate(eventLog, fps);
    }

    public String getRelativePathFixRate(List<MouseMoveEvent> eventLog, int fps) {
        if (eventLog.size() < 2) return "";
        List<MouseMoveEvent> filteredEventLog = filterEventLogByFps(eventLog, fps);
        return getRelativePath(filteredEventLog);
    }


    public String getRelativePathFixRateWithDelays(int fps) {
        return getRelativePathFixRateWithDelays(eventLog, fps);
    }

    public String getRelativePathFixRateWithDelays(List<MouseMoveEvent> eventLog, int fps) {
        if (eventLog.size() < 2) return "";
        List<MouseMoveEvent> filteredEventLog = filterEventLogByFps(eventLog, fps);
        return getRelativePathWithDelays(filteredEventLog);
    }

    private List<MouseMoveEvent> filterEventLogByFps(List<MouseMoveEvent> eventLog, int fps) {
        long frameTime = 1000 / fps;
        long currentTime = eventLog.get(0).getTime();

        List<MouseMoveEvent> filteredEventLog = new ArrayList<>();

        for (int i = 0; i < eventLog.size(); i++) {
            if (eventLog.get(i).getTime() >= currentTime) {
                filteredEventLog.add(eventLog.get(i));
                while (eventLog.get(i).getTime() >= currentTime) {
                    currentTime += frameTime;
                }
            }
        }
        return filteredEventLog;
    }

    public void clear() {
        eventLog.clear();
    }

}
