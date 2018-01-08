package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.events.Event;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActionEncoder implements ActionEncoder {
    private static final int SHIFT = 13500;

    private boolean includeKeys = false;

    private boolean includeMouseButtons = false;

    private boolean includeMouseWheel = false;
    private boolean includeDelays = false;

    private boolean relative = false;

    private boolean absolute = false;
    private boolean isFixedRate = false;

    private boolean isMinDistance = false;
    private int fixedRate = 30;
    private int minDistance = 30;

    public static int getSHIFT() {
        return SHIFT;
    }

    public boolean isIncludesKeys() {
        return includeKeys;
    }

    public boolean isIncludesMouseButtons() {
        return includeMouseButtons;
    }

    public boolean isIncludesMouseWheel() {
        return includeMouseWheel;
    }

    public boolean isIncludesDelays() {
        return includeDelays;
    }

    public boolean isRelative() {
        return relative;
    }

    public boolean isAbsolute() {
        return absolute;
    }

    public boolean isFixedRate() {
        return isFixedRate;
    }

    public int getFixedRate() {
        return fixedRate;
    }

    public boolean isMinDistance() {
        return isMinDistance;
    }

    public int getMinDistance() {
        return minDistance;
    }


    /**
     * Encode events with selected option to string representation
     *
     * @return a string representation of included events
     */
    public abstract String encode(List<Event> eventList);

    protected abstract String encode(int i);

    /**
     * Resets all options to default
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder begin() {
        includeKeys = false;

        includeMouseButtons = false;
        includeMouseWheel = false;

        includeDelays = false;

        relative = false;
        absolute = false;

        isFixedRate = false;
        fixedRate = 30;
        return this;
    }

    /**
     * Add keyboard events
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder addKeys() {
        includeKeys = true;
        return this;
    }

    /**
     * Add mouse buttons events
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder addMouseButtons() {
        includeMouseButtons = true;
        return this;
    }

    /**
     * Add mouse wheel events
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder addMouseWheel() {
        includeMouseWheel = true;
        return this;
    }

    /**
     * Add mouse events
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder addMouse() {
        includeMouseButtons = true;
        includeMouseWheel = true;
        return this;
    }

    /**
     * Add delays
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder addDelays() {
        includeDelays = true;
        return this;
    }

    /**
     * Movement path should be relative
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder relative() {
        relative = true;
        absolute = false;
        return this;
    }

    /**
     * Movement path should be absolute
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder absolute() {
        absolute = true;
        relative = false;
        return this;
    }

    /**
     * Filter movement events with fix fixedRate
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder fixedRate(int fixedRate) {
        isFixedRate = true;
        this.fixedRate = fixedRate;
        return this;
    }

    /**
     * Filter movement events with fix minDistance
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder minDistance(int minDistance) {
        this.isMinDistance = true;
        this.minDistance = minDistance;
        return this;
    }

    List<Event> filter(List<Event> eventList) {
        List<Event> filteredEventList = new ArrayList<>();

        // filter not selected events
        for (Event e : eventList) {
            switch (e.getType()) {
                case KEYBOARD:
                    if (includeKeys) filteredEventList.add(e);
                    break;
                case MOUSE_BUTTON:
                    if (includeMouseButtons) filteredEventList.add(e);
                    break;
                case MOUSE_WHEEL:
                    if (includeMouseWheel) filteredEventList.add(e);
                    break;
                case MOUSE_MOVE:
                    if (relative || absolute) filteredEventList.add(e);
                    break;
            }
        }

        // if absolute or relative path selected
        if (relative || absolute) {
            // then filter movement event
            filteredEventList = filterMovementEvents(filteredEventList);
        }

        return filteredEventList;
    }

    private List<Event> filterMovementEvents(List<Event> eventList) {
        List<Event> filteredEventList = new ArrayList<>();
        List<MouseMoveEvent> moveEventList = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            Event e = eventList.get(i);

            if (e instanceof MouseMoveEvent) {
                // if it is mouse movement event add it to list
                moveEventList.add((MouseMoveEvent) e);

                if (i == eventList.size() - 1) {
                    // if it is last event in the sequence filter mouse movement events
                    List<MouseMoveEvent> filteredMoveEventList = filterMovementPathWithOptions(moveEventList);
                    // add them to result list
                    filteredEventList.addAll(filteredMoveEventList);
                }
            } else {
                // if not
                // filter mouse movement events
                List<MouseMoveEvent> filteredMoveEventList = filterMovementPathWithOptions(moveEventList);
                // add them to result list
                filteredEventList.addAll(filteredMoveEventList);
                // add current event
                filteredEventList.add(e);
            }

        }
        return filteredEventList;
    }

    private List<MouseMoveEvent> filterMovementPathWithOptions(List<MouseMoveEvent> mouseMoveEvents) {
        List<MouseMoveEvent> filteredMoveEventList = new ArrayList<>();
        if (isFixedRate) {
            filteredMoveEventList = filterMoveEventsByFixedRate(mouseMoveEvents, fixedRate);
        }
        if (isMinDistance) {
            filteredMoveEventList = filterMoveEventsByMinDistance(mouseMoveEvents, minDistance);
        }
        // add them to result list
        return filteredMoveEventList;
    }

    /**
     * Filter mouse movement event by fixedRate. Always includes first and last event.
     *
     * @param mouseMoveEvents mouse movement events
     * @param rate            fixedRate per seconds
     * @return filtered list of movement events
     */
    private List<MouseMoveEvent> filterMoveEventsByFixedRate(List<MouseMoveEvent> mouseMoveEvents, int rate) {
        // if event list size less then 3 return without changing
        if (mouseMoveEvents.size() < 3) return mouseMoveEvents;

        long frameTime = 1000 / rate;
        long currentTime = mouseMoveEvents.get(0).getTime();

        List<MouseMoveEvent> filteredEventList = new ArrayList<>();

        for (int i = 0; i < mouseMoveEvents.size(); i++) {
            if (i == mouseMoveEvents.size() - 1) {
                // if it is last event in the list add it to filtered list and break
                filteredEventList.add(mouseMoveEvents.get(i));
                break;
            }

            if (mouseMoveEvents.get(i).getTime() >= currentTime) {
                // if event time bigger then current time then add it to filtered list
                filteredEventList.add(mouseMoveEvents.get(i));
                // increment current time while time of event bigger then current time
                while (mouseMoveEvents.get(i).getTime() >= currentTime) {
                    currentTime += frameTime;
                }
            }
        }
        return filteredEventList;
    }

    /**
     * Filter mouse movement event by fixed minDistance. Always includes first and last event.
     *
     * @param mouseMoveEvents mouse movement events
     * @param distance        minimal minDistance between two points
     * @return filtered list of movement events
     */
    private List<MouseMoveEvent> filterMoveEventsByMinDistance(List<MouseMoveEvent> mouseMoveEvents, int distance) {
        // if event list size less then 3 return without changing
        if (mouseMoveEvents.size() < 3) return mouseMoveEvents;
        MouseMoveEvent lastMouseMoveEvent = mouseMoveEvents.get(0);
        int currentDistance;
        List<MouseMoveEvent> filteredEventList = new ArrayList<>();

        for (int i = 0; i < mouseMoveEvents.size(); i++) {
            MouseMoveEvent e = mouseMoveEvents.get(i);
            // if it is last or first event in the list
            if ((i == mouseMoveEvents.size() - 1) || (i == 0)) {
                // add it to filtered list and break
                filteredEventList.add(e);
                break;
            }
            // calculate minDistance to last mouse event
            int dx = e.getX() - lastMouseMoveEvent.getX();
            int dy = e.getY() - lastMouseMoveEvent.getY();
            currentDistance = (int) Math.sqrt(dx * dx + dy * dy);
            // if current minDistance bigger then specified
            if (currentDistance > distance) {
                // add event to filtered list
                filteredEventList.add(e);
                // set current mouse event as last event
                lastMouseMoveEvent = e;
            }
        }
        return filteredEventList;
    }
}
