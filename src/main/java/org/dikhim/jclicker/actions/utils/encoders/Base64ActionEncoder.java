package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.utils.encoding.Base64Encoder;

import java.util.Base64;

import static org.dikhim.jclicker.actions.actions.ActionType.*;
import static org.dikhim.jclicker.actions.actions.ActionType.DELAY_SECONDS;

public class Base64ActionEncoder extends AbstractActionEncoder {
    private Base64Encoder encoder = new Base64Encoder();
    public Base64ActionEncoder() {
        putActionCode(KEYBOARD_PRESS, "<");
        putActionCode(KEYBOARD_RELEASE, ">");

        putActionCode(MOUSE_MOVE, "#");
        putActionCode(MOUSE_MOVE_TO, "@");

        putActionCode(MOUSE_PRESS_LEFT, "(");
        putActionCode(MOUSE_PRESS_RIGHT, "{");
        putActionCode(MOUSE_PRESS_MIDDLE, "[");

        putActionCode(MOUSE_RELEASE_LEFT, ")");
        putActionCode(MOUSE_RELEASE_RIGHT, "}");
        putActionCode(MOUSE_RELEASE_MIDDLE, "]");

        putActionCode(MOUSE_WHEEL_DOWN, "$");
        putActionCode(MOUSE_WHEEL_UP, "%");

        putActionCode(DELAY_MILLISECONDS, "-");
        putActionCode(DELAY_SECONDS, "*");
    }

    protected String encodeParameter(int i) {
        return new String(encoder.encodeTo3Chars(i));
    }
}
