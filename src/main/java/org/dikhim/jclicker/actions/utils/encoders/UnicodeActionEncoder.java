package org.dikhim.jclicker.actions.utils.encoders;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.KeyCodes;

import static org.dikhim.jclicker.actions.actions.ActionType.*;


@SuppressWarnings("Duplicates")
public class UnicodeActionEncoder extends AbstractActionEncoder {
    private static final int SHIFT = 13500;
    
    private static final BidiMap<ActionType, String> actionCodes;

    static {
        actionCodes = new DualHashBidiMap<>();
        
        actionCodes.put(KEYBOARD_PRESS, "k");
        actionCodes.put(KEYBOARD_RELEASE, "K");

        actionCodes.put(MOUSE_MOVE, "X");
        actionCodes.put(MOUSE_MOVE_TO, "A");

        actionCodes.put(MOUSE_PRESS_LEFT, "l");
        actionCodes.put(MOUSE_PRESS_RIGHT, "r");
        actionCodes.put(MOUSE_PRESS_MIDDLE, "m");

        actionCodes.put(MOUSE_RELEASE_LEFT, "L");
        actionCodes.put(MOUSE_RELEASE_RIGHT, "R");
        actionCodes.put(MOUSE_RELEASE_MIDDLE, "M");

        actionCodes.put(MOUSE_WHEEL_UP, "W");
        actionCodes.put(MOUSE_WHEEL_DOWN, "w");

        actionCodes.put(DELAY_MILLISECONDS, "D");
        actionCodes.put(DELAY_SECONDS, "S");
    }

    public static BidiMap<ActionType, String> getActionCodes() {
        return actionCodes;
    }

    protected String encodeAction(Action action) {
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
                result += encodeParameter(((MouseMoveAtAction) action).getX());
                result += encodeParameter(((MouseMoveAtAction) action).getY());
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
                System.out.println(((DelayMillisecondsAction) action).getDelay());
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
        return actionCodes.get(actionType);
    }
    
    private String encodeParameter(int i) {
        return Character.toString((char) (i + SHIFT));
    }

    public static int getSHIFT() {
        return SHIFT;
    }
}
