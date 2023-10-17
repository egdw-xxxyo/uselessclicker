package org.dikhim.clickauto.jsengine.utils.encoders;


import static org.dikhim.clickauto.jsengine.actions.ActionType.*;

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

    protected String encodeParameter(int i) {
        return Character.toString((char) (i + SHIFT));
    }
}
