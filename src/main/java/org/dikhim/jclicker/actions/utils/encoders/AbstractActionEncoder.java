package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.events.Event;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActionEncoder implements ActionEncoder {
    private static final int SHIFT = 13500;

    public static int getSHIFT() {
        return SHIFT;
    }

    public boolean isIncludesKeys() {
        return includeKeys;
    }

    public boolean isIncludesMouseMovement() {
        return includeMouseMovement;
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

    public boolean isFixRate() {
        return fixRate;
    }

    public int getRate() {
        return rate;
    }

    private boolean includeKeys = false;

    private boolean includeMouseMovement = false;
    private boolean includeMouseButtons = false;
    private boolean includeMouseWheel = false;

    private boolean includeDelays = false;

    private boolean relative = false;

    private boolean fixRate = false;
    private int rate = 30;


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

        includeMouseMovement = false;
        includeMouseButtons = false;
        includeMouseWheel = false;

        includeDelays = false;

        relative = false;

        fixRate = false;
        rate = 30;
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
     * Add movement events
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder addMouseMovement() {
        includeMouseMovement = true;
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
        includeMouseMovement = true;
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
        return this;
    }

    /**
     * Movement path should be absolute
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder absolute() {
        relative = false;
        return this;
    }

    /**
     * Filter movement events with fix rate
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder fixRate(int rate) {
        fixRate = true;
        this.rate = rate;
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
                    if (includeMouseMovement) filteredEventList.add(e);
                    break;
            }
        }

        // filter for fix rate
        if (fixRate) {
            filteredEventList = filterEventListByRate(filteredEventList, rate);
        }

        return filteredEventList;
    }

    private List<Event> filterEventListByRate(List<Event> eventList, int rate) {
        List<Event> filteredEventList = new ArrayList<>();
        List<MouseMoveEvent> moveEventList = new ArrayList<>();
        for (int i = 0; i < eventList.size(); i++) {
            Event e = eventList.get(i);

            if (e instanceof MouseMoveEvent) {
                // if it is mouse movement event add it to list
                moveEventList.add((MouseMoveEvent) e);

                if (i == eventList.size() - 1) {
                    // if it is last event in the sequence filter mouse movement events
                    List<MouseMoveEvent> filteredMoveEventList = filterMoveEventsByRate(moveEventList, rate);
                    // add them to result list
                    filteredEventList.addAll(filteredMoveEventList);
                }
            } else {
                // if not filter mouse movement events
                List<MouseMoveEvent> filteredMoveEventList = filterMoveEventsByRate(moveEventList, rate);
                // add them to result list
                filteredEventList.addAll(filteredMoveEventList);
                // add current event
                filteredEventList.add(e);
            }

        }
        return filteredEventList;
    }

    /**
     * Filter mouse movement event by rate. Always includes first and last event.
     *
     * @param eventList mouse movement events
     * @param rate      rate per seconds
     * @return filtered list of movement events
     */
    private List<MouseMoveEvent> filterMoveEventsByRate(List<MouseMoveEvent> eventList, int rate) {
        // if event list size less then 3 return without changing
        if (eventList.size() < 3) return eventList;

        long frameTime = 1000 / rate;
        long currentTime = eventList.get(0).getTime();

        List<MouseMoveEvent> filteredEventList = new ArrayList<>();

        for (int i = 0; i < eventList.size(); i++) {
            if (i == eventList.size() - 1) {
                // if it is last event in the list add it to filtered list and break
                filteredEventList.add(eventList.get(i));
                break;
            }

            if (eventList.get(i).getTime() >= currentTime) {
                // if event time bigger then current time then add it to filtered list
                filteredEventList.add(eventList.get(i));
                // increment current time while time of event bigger then current time
                while (eventList.get(i).getTime() >= currentTime) {
                    currentTime += frameTime;
                }
            }
        }
        return filteredEventList;
    }
}
