package org.dikhim.jclicker.actions.utils;

import org.dikhim.jclicker.actions.events.MouseMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class MouseMoveEventUtil {
    private List<MouseMoveEvent> eventLog = new ArrayList<>();

    private final int SHIFT = 13350;

    public void add(MouseMoveEvent mouseMoveEvent) {
        eventLog.add(mouseMoveEvent);
    }

    public void addAll(List<MouseMoveEvent> list) {
        eventLog.addAll(list);
    }

    public List<MouseMoveEvent> getEventLog() {
        return eventLog;
    }

    public void setEventLog(List<MouseMoveEvent> eventLog) {
        this.eventLog = eventLog;
    }


    public String getAbsolutePath(int lineSize) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");

        sb.append("mouse.moveAbsolute('");
        for (MouseMoveEvent e : eventLog) {
            sb.append((char) (e.getX() + SHIFT));
            sb.append((char) (e.getY() + SHIFT));
        }
        sb.append("');\n");

        return separateOnLines(sb, lineSize);
    }

    public String getAbsolutePathWithDelays(List<MouseMoveEvent> eventLog, int lineSize) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");
        long delay;

        sb.append("mouse.moveAbsolute_D('");
        for (int i = 0; i < eventLog.size(); i++) {
            sb.append((char) (eventLog.get(i).getX() + SHIFT));
            sb.append((char) (eventLog.get(i).getY() + SHIFT));

            if (i < eventLog.size() - 1) {
                delay = eventLog.get(i + 1).getTime() - eventLog.get(i).getTime();
            } else {
                delay = 0;
            }
            sb.append((char) (delay + SHIFT));
        }
        sb.append("');\n");

        return separateOnLines(sb, lineSize);
    }

    public String getAbsolutePathWithDelays(int lineSize) {
        return getAbsolutePathWithDelays(eventLog, lineSize);
    }

    public String getAbsolutePathFixRateWithDelays(int fps, int lineSize) {
        if (eventLog.size() < 2) return "";

        List<MouseMoveEvent> newEventLog = getEventLogFixRate(fps, eventLog);

        return getAbsolutePathWithDelays(newEventLog, lineSize);
    }

    public String getRelativePath(int lineSize) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");
        int oldX = eventLog.get(0).getX();
        int oldY = eventLog.get(0).getY();
        int dx, dy;

        sb.append("mouse.moveRelative('");
        for (int i = 0; i < eventLog.size(); i++) {
            dx = eventLog.get(i).getX() - oldX;
            dy = eventLog.get(i).getY() - oldY;
            oldX = eventLog.get(i).getX();
            oldY = eventLog.get(i).getY();

            sb.append((char) (dx + SHIFT));
            sb.append((char) (dy + SHIFT));


        }
        sb.append("');\n");

        return separateOnLines(sb, lineSize);
    }

    private String getRelativePathWithDelays(List<MouseMoveEvent> eventLog, int lineSize) {
        if (eventLog.size() < 2) return "";
        StringBuilder sb = new StringBuilder("");
        int oldX = eventLog.get(0).getX();
        int oldY = eventLog.get(0).getY();
        int dx, dy;
        long delay;

        sb.append("mouse.moveRelative_D('");
        for (int i = 1; i < eventLog.size(); i++) {
            dx = eventLog.get(i).getX() - oldX;
            dy = eventLog.get(i).getY() - oldY;
            oldX = eventLog.get(i).getX();
            oldY = eventLog.get(i).getY();
            sb.append((char) (dx + SHIFT));
            sb.append((char) (dy + SHIFT));

            if (i < eventLog.size() - 1) {
                delay = eventLog.get(i + 1).getTime() - eventLog.get(i).getTime();
            } else {
                delay = 0;
            }
            sb.append((char) (delay + SHIFT));
        }
        sb.append("');\n");

        return separateOnLines(sb, lineSize);
    }

    public String getRelativePathWithDelays(int lineSize) {
        return getRelativePathWithDelays(eventLog, lineSize);
    }

    public String getRelativePathFixRateWithDelays(int fps, int lineSize) {
        if (eventLog.size() < 2) return "";

        List<MouseMoveEvent> newEventLog = getEventLogFixRate(fps, eventLog);

        return getRelativePathWithDelays(newEventLog, lineSize);
    }

    private List<MouseMoveEvent> getEventLogFixRate(int fps,List<MouseMoveEvent> eventLog){
        long frameTime = 1000 / fps;
        long currentTime = eventLog.get(0).getTime();

        List<MouseMoveEvent> newEventLog = new ArrayList<>();

        for (int i = 0; i < eventLog.size(); i++) {
            if (eventLog.get(i).getTime() >= currentTime) {
                newEventLog.add(eventLog.get(i));
                while (eventLog.get(i).getTime() >= currentTime) {
                    currentTime += frameTime;
                }
            }
        }
        return newEventLog;
    }
    private String separateOnLines(StringBuilder inputStringBuilder, int lineSize) {
        char[] c = inputStringBuilder.toString().toCharArray();
        StringBuilder sb = new StringBuilder("");

        for(int i=0;i<c.length-4;i++){

            if ((sb.length() + 3) % lineSize != 0) {
                sb.append(c[i]);
            } else {
                sb.append("'+\n");
                sb.append('\'');
                sb.append(c[i]);
            }
        }
        sb.append("');\n");
        return sb.toString();
    }


    public void clear() {
        eventLog.clear();
    }

}
