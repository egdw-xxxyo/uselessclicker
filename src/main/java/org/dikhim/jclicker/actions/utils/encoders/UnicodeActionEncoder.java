package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.KeyCodes;

import static org.dikhim.jclicker.actions.actions.ActionType.*;

@SuppressWarnings("Duplicates")
public class UnicodeActionEncoder extends AbstractActionEncoder {

    public static final int SHIFT = 13500;
    
    public UnicodeActionEncoder() {
        putActionCode(KEYBOARD_PRESS, "k");
        putActionCode(KEYBOARD_RELEASE, "K");

        putActionCode(MOUSE_MOVE, "X");
        putActionCode(MOUSE_MOVE_TO, "A");

        putActionCode(MOUSE_PRESS_LEFT, "l");
        putActionCode(MOUSE_PRESS_RIGHT, "r");
        putActionCode(MOUSE_PRESS_MIDDLE, "m");

        putActionCode(MOUSE_RELEASE_LEFT, "L");
        putActionCode(MOUSE_RELEASE_RIGHT, "R");
        putActionCode(MOUSE_RELEASE_MIDDLE, "M");

        putActionCode(MOUSE_WHEEL_UP, "W");
        putActionCode(MOUSE_WHEEL_DOWN, "w");

        putActionCode(DELAY_MILLISECONDS, "D");
        putActionCode(DELAY_SECONDS, "S");
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

    private String encodeParameter(int i) {
        return Character.toString((char) (i + SHIFT));
    }
}
