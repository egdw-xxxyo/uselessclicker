package org.dikhim.jclicker.actions.utils.encoders;

import java.util.HashMap;
import java.util.Map;

public class ActionEncoder {
    private final int SHIFT = 13500;

    private Map<ActionType,Integer> actionCodes = new HashMap<>();

    {
        actionCodes.put(ActionType.KEYBOARD_PRESS,0);
        actionCodes.put(ActionType.KEYBOARD_RELEASE,1);

        actionCodes.put(ActionType.MOUSE_MOVE,2);
        actionCodes.put(ActionType.MOUSE_MOVE_AT,3);

        actionCodes.put(ActionType.MOUSE_PRESS_LEFT,4);
        actionCodes.put(ActionType.MOUSE_PRESS_RIGHT,5);
        actionCodes.put(ActionType.MOUSE_PRESS_MIDDLE,6);

        actionCodes.put(ActionType.MOUSE_RELEASE_LEFT,7);
        actionCodes.put(ActionType.MOUSE_RELEASE_RIGHT,8);
        actionCodes.put(ActionType.MOUSE_RELEASE_MIDDLE,9);

        actionCodes.put(ActionType.MOUSE_WHEEL_UP,10);

        actionCodes.put(ActionType.MOUSE_WHEEL_DOWN,11);

        actionCodes.put(ActionType.DELAY,12);
    }
}
