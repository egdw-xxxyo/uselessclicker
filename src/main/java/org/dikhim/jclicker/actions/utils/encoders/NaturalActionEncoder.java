package org.dikhim.jclicker.actions.utils.encoders;

import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.utils.encoding.NaturalEncoder;

import static org.dikhim.jclicker.actions.actions.ActionType.*;
import static org.dikhim.jclicker.actions.actions.ActionType.DELAY_SECONDS;

public class NaturalActionEncoder extends AbstractActionEncoder {

    private NaturalEncoder encoder = new NaturalEncoder();
    public NaturalActionEncoder() {
        putActionCode(KEYBOARD_PRESS, "k:");
        putActionCode(KEYBOARD_RELEASE, "K:");

        putActionCode(MOUSE_MOVE, "X:");
        putActionCode(MOUSE_MOVE_TO, "A:");

        putActionCode(MOUSE_PRESS_LEFT, "l:");
        putActionCode(MOUSE_PRESS_RIGHT, "r:");
        putActionCode(MOUSE_PRESS_MIDDLE, "m:");

        putActionCode(MOUSE_RELEASE_LEFT, "L:");
        putActionCode(MOUSE_RELEASE_RIGHT, "R:");
        putActionCode(MOUSE_RELEASE_MIDDLE, "M:");

        putActionCode(MOUSE_WHEEL_UP, "W");
        putActionCode(MOUSE_WHEEL_DOWN, "w");

        putActionCode(DELAY_MILLISECONDS, "D");
        putActionCode(DELAY_SECONDS, "S");
    }

    @Override
    protected String encodeParameter(int i) {
        return encoder.encode(i)+" ";
    }

    @Override
    protected String encodeKeyboardKey(int i) {
        return KeyCodes.getNameByUselessCode(i);
    }
}
