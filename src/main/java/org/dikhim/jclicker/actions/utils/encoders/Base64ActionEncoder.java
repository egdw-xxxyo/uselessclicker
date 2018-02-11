package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.KeyCodes;

import java.util.Base64;

import static org.dikhim.jclicker.actions.actions.ActionType.*;
import static org.dikhim.jclicker.actions.actions.ActionType.DELAY_SECONDS;

public class Base64ActionEncoder extends AbstractActionEncoder {
    public Base64ActionEncoder() {
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

    protected String encodeParameter(int i) {
        short param = (short) i;
        byte[] byteArray = new byte[2];
        byteArray[0] = (byte) (param & 0xff);
        byteArray[1] = (byte) ((param >> 8) & 0xff); 
        return Base64.getEncoder().encodeToString(byteArray);
    }
}
