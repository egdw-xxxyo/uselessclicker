package org.dikhim.clickauto.jsengine.utils.encoders;


import org.dikhim.clickauto.jsengine.utils.encoding.Base64Encoder;

import static org.dikhim.clickauto.jsengine.actions.ActionType.*;

public class Base64ActionEncoder extends AbstractActionEncoder {
    private Base64Encoder encoder = new Base64Encoder();

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
        return new String(encoder.encodeTo3Chars(i));
    }
}
