package org.dikhim.jclicker.actions.utils.encoders;

import static org.dikhim.jclicker.actions.actions.ActionType.*;
import static org.dikhim.jclicker.actions.actions.ActionType.DELAY_SECONDS;

public class NaturalActionEncoder extends AbstractActionEncoder {
    @Override
    protected String encodeParameter(int i) {
        return String.valueOf(i);
    }

    public NaturalActionEncoder() {
        putActionCode(KEYBOARD_PRESS, "KEY_P");
        putActionCode(KEYBOARD_RELEASE, "KEY_R");

        putActionCode(MOUSE_MOVE, "MOVE");
        putActionCode(MOUSE_MOVE_TO, "MOVE_TO");

        putActionCode(MOUSE_PRESS_LEFT, "P_LEFT");
        putActionCode(MOUSE_PRESS_RIGHT, "P_RIGHT");
        putActionCode(MOUSE_PRESS_MIDDLE, "P_MIDDLE");

        putActionCode(MOUSE_RELEASE_LEFT, "R_LEFT");
        putActionCode(MOUSE_RELEASE_RIGHT, "R_RIGHT");
        putActionCode(MOUSE_RELEASE_MIDDLE, "R_MIDDLE");

        putActionCode(MOUSE_WHEEL_UP, "WHEEL_U");
        putActionCode(MOUSE_WHEEL_DOWN, "WHEEL_D");

        putActionCode(DELAY_MILLISECONDS, "D");
        putActionCode(DELAY_SECONDS, "SECONDS");
    }

    @Override
    public String encodeKey(String key) {
        return key;
    }


    @Override
    public String delimiterActionEnd() {
        return "; ";
    }

    @Override
    public String delimiterBeforeParams() {
        return "(";
    }

    @Override
    public String delimiterAfterParams() {
        return ")";
    }

    @Override
    public String delimiterBetweenParams() {
        return ",";
    }
}
