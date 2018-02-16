package org.dikhim.jclicker.actions.utils.encoders;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.events.*;
import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.util.output.Out;

import java.util.ArrayList;
import java.util.List;

import static org.dikhim.jclicker.actions.actions.ActionType.*;
import static org.dikhim.jclicker.actions.actions.ActionType.DELAY_SECONDS;

public abstract class AbstractActionEncoder implements ActionEncoder {
    private static final BidiMap<ActionType, String> actionCodes = new DualHashBidiMap<>();
    
    public static final int MAX_DELAY_MILLISECONDS = 1000;
    public static final int MAX_DELAY_SECONDS = 3600;
    public static final int MAX_COORDINATE = 10000;
    public static final int MAX_D_COORDINATE = 10000;
    public static final int MIN_D_COORDINATE = -10000;
    public static final int MAX_WHEEL_AMOUNT = 100;



    private boolean isAbsolute = false;
    private boolean isRelative = false;


    private boolean includeKeys = false;
    private boolean includeMouseButtons = false;
    private boolean includeMouseWheel = false;
    private boolean includeDelays = false;


    private boolean isFixedRate = false;
    private int fixedRate = 30;

    private boolean isMinDistance = false;
    private int minDistance = 30;

    private boolean detectStopPoints = false;

    private int stopPointThreshold = 50;


    


    /**
     * Encode events with selected option to string representation
     *
     * @return a string representation of included events
     */
    public String encode(List<Event> eventList) {
        List<Event> filteredEventList = filter(eventList);

        List<Action> actions = convertToActionList(filteredEventList);

        StringBuilder sb = new StringBuilder();
        for (Action a : actions) {
            sb.append(encodeAction(a));
        }
        return sb.toString();
    }

    private String encodeAction(Action action) {
        String result = "";
        switch (action.getType()) {
            case KEYBOARD_PRESS:
                result += encodeActionType(KEYBOARD_PRESS);
                result += encodeParameter(
                        KeyCodes.getUslessCodeByName(((KeyboardPressAction) action).getKey()));
                break;
            case KEYBOARD_RELEASE:
                result += encodeActionType(KEYBOARD_RELEASE);
                result += encodeParameter(
                        KeyCodes.getUslessCodeByName(((KeyboardPressAction) action).getKey()));
                break;
            case MOUSE_MOVE:
                result += encodeActionType(MOUSE_MOVE);
                result += encodeParameter(((MouseMoveAction) action).getDx());
                result += encodeParameter(((MouseMoveAction) action).getDy());
                break;
            case MOUSE_MOVE_TO:
                result += encodeActionType(MOUSE_MOVE_TO);
                result += encodeParameter(((MouseMoveToAction) action).getX());
                result += encodeParameter(((MouseMoveToAction) action).getY());
                break;
            case MOUSE_PRESS_LEFT:
                result += encodeActionType(MOUSE_PRESS_LEFT);
                break;
            case MOUSE_PRESS_RIGHT:
                result += encodeActionType(MOUSE_PRESS_RIGHT);
                break;
            case MOUSE_PRESS_MIDDLE:
                result += encodeActionType(MOUSE_PRESS_MIDDLE);
                break;
            case MOUSE_RELEASE_LEFT:
                result += encodeActionType(MOUSE_RELEASE_LEFT);
                break;
            case MOUSE_RELEASE_RIGHT:
                result += encodeActionType(MOUSE_RELEASE_RIGHT);
                break;
            case MOUSE_RELEASE_MIDDLE:
                result += encodeActionType(MOUSE_RELEASE_MIDDLE);
                break;
            case MOUSE_WHEEL_UP:
                result += encodeActionType(MOUSE_WHEEL_UP);
                result += encodeParameter(((MouseWheelUpAction) action).getAmount());
                break;
            case MOUSE_WHEEL_DOWN:
                result += encodeActionType(MOUSE_WHEEL_DOWN);
                result += encodeParameter(((MouseWheelDownAction) action).getAmount());
                break;
            case DELAY_MILLISECONDS:
                result += encodeActionType(DELAY_MILLISECONDS);
                result += encodeParameter(((DelayMillisecondsAction) action).getDelay());
                break;
            case DELAY_SECONDS:
                result += encodeActionType(DELAY_SECONDS);
                result += encodeParameter(((DelaySecondsAction) action).getDelay());
                break;
        }
        return result;
    }

    private String encodeActionType(ActionType actionType) {
        return getActionCode(actionType);
    }

    protected abstract String encodeParameter(int i);

    protected String encodeKeyboardKey(int i) {
       return encodeParameter(i);
    }
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

        isRelative = false;
        isAbsolute = false;

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
     * Add delays
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder addDelays() {
        includeDelays = true;
        return this;
    }

    /**
     * Movement path should be isRelative
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder relative() {
        isRelative = true;
        isAbsolute = false;
        return this;
    }

    /**
     * Movement path should be isAbsolute
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder absolute() {
        isAbsolute = true;
        isRelative = false;
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
     * Filter movement events with minDistance
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder minDistance(int minDistance) {
        this.isMinDistance = true;
        this.minDistance = minDistance;
        return this;
    }

    /**
     * Filter movement events with minDistance
     *
     * @return a reference to this object.
     */
    public AbstractActionEncoder detectStopPoints(int stopPointDetectionThreshold) {
        this.detectStopPoints = true;
        this.stopPointThreshold = stopPointDetectionThreshold;
        return this;
    }


    /**
     * Filter list of events with specified options
     *
     * @param eventList list of all recorded events
     * @return filtered list
     */
    private List<Event> filter(List<Event> eventList) {
        List<Event> filteredEventList = new ArrayList<>();

        // filter not selected events
        for (Event e : eventList) {
            switch (e.getType()) {
                case KEYBOARD:
                    if (isIncludesKeys()) filteredEventList.add(e);
                    break;
                case MOUSE_BUTTON:
                    if (isIncludesMouseButtons()) filteredEventList.add(e);
                    break;
                case MOUSE_WHEEL:
                    if (isIncludesMouseWheel()) filteredEventList.add(e);
                    break;
                case MOUSE_MOVE:
                    if (isRelative() || isAbsolute()) filteredEventList.add(e);
                    break;
            }
        }

        // filter movement events
        filteredEventList = filterMovementEvents(filteredEventList);

        return filteredEventList;
    }

    /**
     * Filter movement events with specified options.<br>
     * All not movement events always are included to the result list
     *
     * @param eventList list of all recorded events
     * @return filtered list
     */
    private List<Event> filterMovementEvents(List<Event> eventList) {
        if (!isRelative && !isAbsolute) return eventList;
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
                moveEventList.clear();
                // add them to result list
                filteredEventList.addAll(filteredMoveEventList);
                // add current event
                filteredEventList.add(e);
            }

        }
        return filteredEventList;
    }

    private List<MouseMoveEvent> filterMovementPathWithOptions(List<MouseMoveEvent> mouseMoveEvents) {
        // TODO Create 1 method for filtering
        List<MouseMoveEvent> filteredMoveEventList = new ArrayList<>(mouseMoveEvents);
        if (isFixedRate()) {
            filteredMoveEventList = filterMoveEventsByFixedRate(filteredMoveEventList);
        }
        if (isMinDistance()) {
            filteredMoveEventList = filterMoveEventsByMinDistance(filteredMoveEventList);
        }
        // add them to result list
        return filteredMoveEventList;
    }

    /**
     * Filter mouse movement event by fixedRate. Always includes first and last event.
     *
     * @param mouseMoveEvents mouse movement events
     * @return filtered list of movement events
     */
    private List<MouseMoveEvent> filterMoveEventsByFixedRate(List<MouseMoveEvent> mouseMoveEvents) {
        // if event list size less then 3 return without changing
        if (mouseMoveEvents.size() < 3) return mouseMoveEvents;

        long frameTime = 1000 / getFixedRate();
        long currentTime = mouseMoveEvents.get(0).getTime();

        List<MouseMoveEvent> filteredEventList = new ArrayList<>();

        for (int i = 0; i < mouseMoveEvents.size(); i++) {
            MouseMoveEvent e = mouseMoveEvents.get(i);
            // if it is last or first event in the list
            if ((i == mouseMoveEvents.size() - 1) || (i == 0)) {
                // add it to filtered list and break
                filteredEventList.add(e);
                continue;
            }
            // if detection stop point option on
            if (isDetectStopPoints()) {
                // calculate delay to previous event
                int delay = (int) (mouseMoveEvents.get(i + 1).getTime() - e.getTime());
                // id delay bigger then threshold for stop point detection
                if (delay >= stopPointThreshold) {
                    // add event to filtered list
                    filteredEventList.add(e);
                    continue;
                }
            }
            if (e.getTime() >= currentTime) {
                // if event time bigger then current time then add it to filtered list
                filteredEventList.add(e);
                // increment current time while time of event bigger then current time
                while (e.getTime() >= currentTime) {
                    currentTime += frameTime;
                }
            }
        }
        return filteredEventList;
    }

    /**
     * Filter mouse movement event by minDistance. Always includes first and last event.
     *
     * @param mouseMoveEvents mouse movement events
     * @return filtered list of movement events
     */
    private List<MouseMoveEvent> filterMoveEventsByMinDistance(List<MouseMoveEvent> mouseMoveEvents) {
        // if event list size less then 3 return without changing
        if (mouseMoveEvents.size() < 3) return mouseMoveEvents;
        int currentDistance;
        List<MouseMoveEvent> filteredEventList = new ArrayList<>();

        for (int i = 0; i < mouseMoveEvents.size(); i++) {
            MouseMoveEvent e = mouseMoveEvents.get(i);
            // if it is last or first event in the list
            if ((i == mouseMoveEvents.size() - 1) || (i == 0)) {
                // add it to filtered list and break
                filteredEventList.add(e);
                continue;
            }
            // if detection stop point option on
            if (isDetectStopPoints()) {
                // calculate delay to previous event
                int delay = (int) (mouseMoveEvents.get(i + 1).getTime() - e.getTime());
                // id delay bigger then threshold for stop point detection
                if (delay >= stopPointThreshold) {
                    // add event to filtered list
                    filteredEventList.add(e);
                    continue;
                }
            }
            // calculate minDistance to last mouse event
            int dx = e.getX() - filteredEventList.get(filteredEventList.size() - 1).getX();
            int dy = e.getY() - filteredEventList.get(filteredEventList.size() - 1).getY();
            currentDistance = (int) Math.sqrt(dx * dx + dy * dy);
            // if current minDistance bigger then specified
            if (currentDistance > getMinDistance()) {
                // add event to filtered list
                filteredEventList.add(e);
            }
        }
        return filteredEventList;
    }


    private List<Action> convertToActionList(List<Event> events) {
        List<Action> actions = new ArrayList<>();
        Event lastMoveEvent = null;

        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);

            if (isIncludesDelays() && i > 0) {
                actions.addAll(createDelayActions(events.get(i - 1), e));
            }
            switch (e.getType()) {
                case KEYBOARD:
                    actions.add(createKeyboardAction((KeyboardEvent) e));
                    break;
                case MOUSE_BUTTON:
                    actions.add(createMouseButtonAction((MouseButtonEvent) e));
                    break;
                case MOUSE_WHEEL:
                    actions.add(createMouseWheelAction((MouseWheelEvent) e));
                    break;
                case MOUSE_MOVE:

                    if (isAbsolute()) {
                        actions.add(createMouseMoveToAction((MouseMoveEvent) e));
                    } else {
                        if (lastMoveEvent == null) {
                            lastMoveEvent = e;
                            break;
                        }
                        actions.add(createMouseMoveAction((MouseMoveEvent) lastMoveEvent, (MouseMoveEvent) e));
                        lastMoveEvent = e;
                    }
                    break;
            }

        }
        return actions;
    }

    private List<Action> createDelayActions(Event previousEvent, Event event) {
        List<Action> actions = new ArrayList<>();
        int delay = (int) (event.getTime() - previousEvent.getTime());
        if (delay < 0) {
            delay = 0;
            Out.println(String.format("delay=%s is less then 0, it has been set to 0", delay));
        }
        if (delay <= MAX_DELAY_MILLISECONDS) {
            actions.add(new DelayMillisecondsAction(delay));
        } else {
            int milliseconds = delay % 1000;
            int seconds = delay / 1000;
            if (seconds > MAX_DELAY_SECONDS) {
                seconds = MAX_DELAY_SECONDS;
                Out.println(String.format("delay=%s is bigger then %s seconds, " +
                        "it has been set to %s seconds", delay, MAX_DELAY_SECONDS, MAX_DELAY_SECONDS));
            }
            actions.add(new DelaySecondsAction(seconds));
            actions.add(new DelayMillisecondsAction(milliseconds));
        }
        return actions;
    }


    private Action createKeyboardAction(KeyboardEvent keyboardEvent) {
        Action result;
        if (keyboardEvent.getAction().equals("PRESS")) {
            result = new KeyboardPressAction(keyboardEvent.getKey());
        } else {
            result = new KeyboardReleaseAction(keyboardEvent.getKey());
        }
        return result;
    }

    private Action createMouseButtonAction(MouseButtonEvent mouseButtonEvent) {
        Action result = null;
        switch (mouseButtonEvent.getButton()) {
            case "LEFT":
                switch (mouseButtonEvent.getAction()) {
                    case "PRESS":
                        result = new MousePressLeftAction();
                        break;
                    case "RELEASE":
                        result = new MouseReleaseLeftAction();
                        break;
                }
                break;
            case "RIGHT":
                switch (mouseButtonEvent.getAction()) {
                    case "PRESS":
                        result = new MousePressRightAction();
                        break;
                    case "RELEASE":
                        result = new MouseReleaseRightAction();
                        break;
                }
                break;
            case "MIDDLE":
                switch (mouseButtonEvent.getAction()) {
                    case "PRESS":
                        result = new MousePressMiddleAction();
                        break;
                    case "RELEASE":
                        result = new MouseReleaseMiddleAction();
                        break;
                }
                break;
        }
        return result;
    }

    private Action createMouseWheelAction(MouseWheelEvent mouseWheelEvent) {
        Action result = null;
        int amount = mouseWheelEvent.getAmount();
        if (amount > MAX_WHEEL_AMOUNT) {
            amount = MAX_WHEEL_AMOUNT;
            Out.println(String.format("wheel amount=%s is bigger then %s, it has been set to %s", amount, MAX_WHEEL_AMOUNT, MAX_WHEEL_AMOUNT));
        }
        switch (mouseWheelEvent.getDirection()) {
            case "DOWN":
                result = new MouseWheelDownAction(amount);
                break;
            case "UP":
                result = new MouseWheelUpAction(amount);
                break;
        }

        return result;
    }

    private Action createMouseMoveToAction(MouseMoveEvent mouseMoveEvent) {
        int x = mouseMoveEvent.getX();
        int y = mouseMoveEvent.getY();
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x > MAX_COORDINATE) {
            x = MAX_COORDINATE;
        }
        if (y > MAX_COORDINATE) {
            y = MAX_COORDINATE;
        }
        return new MouseMoveToAction(x, y);
    }

    private Action createMouseMoveAction(MouseMoveEvent previousEvent, MouseMoveEvent mouseMoveEvent) {
        int dx = mouseMoveEvent.getX() - previousEvent.getX();
        int dy = mouseMoveEvent.getY() - previousEvent.getY();
        if (dx < MIN_D_COORDINATE) {
            dx = MIN_D_COORDINATE;
        }
        if (dy < MIN_D_COORDINATE) {
            dy = MIN_D_COORDINATE;

        }
        if (dx > MAX_D_COORDINATE) {
            dx = MAX_D_COORDINATE;
        }
        if (dy > MAX_D_COORDINATE) {
            dy = MAX_D_COORDINATE;
        }
        return new MouseMoveAction(dx, dy);
    }

    protected void putActionCode(ActionType actionType, String actionCode) {
        actionCodes.put(actionType, actionCode);
    }

    protected String getActionCode(ActionType actionType) {
        return actionCodes.get(actionType);
    }
    
    // getters
    public static BidiMap<ActionType, String> getActionCodes() {
        return actionCodes;
    }
    
    public boolean isDetectStopPoints() {
        return detectStopPoints;
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
        return isRelative;
    }

    public boolean isAbsolute() {
        return isAbsolute;
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

}
