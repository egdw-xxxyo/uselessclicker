package org.dikhim.jclicker.actions.utils.decoders;

import org.apache.commons.collections4.BidiMap;
import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.utils.encoders.UnicodeEncoder;

import java.util.ArrayList;
import java.util.List;

public class UnicodeDecoder implements ActionDecoder {

    private BidiMap<ActionType, Character> actionCodes = UnicodeEncoder.getActionCodes();
    private final int SHIFT = UnicodeEncoder.getSHIFT();

    public List<Action> decode(String code) {
        List<Action> actions = new ArrayList<>();

        char[] codeArray = code.toCharArray();
        int i = 0;
        while (i < codeArray.length) {
            char c = codeArray[i];
            ActionType actionType = actionCodes.getKey(c);

            int intTmp1, intTmp2;
            String strTmp;
            switch (actionType) {
                case KEYBOARD_PRESS:
                    strTmp = KeyCodes.getNameByUselessCode(decode(codeArray[i + 1]));
                    i += 2;
                    if (!strTmp.isEmpty()) actions.add(new KeyboardPressAction(strTmp));
                    break;
                case KEYBOARD_RELEASE:
                    strTmp = KeyCodes.getNameByUselessCode(decode(codeArray[i + 1]));
                    i += 2;
                    if (!strTmp.isEmpty()) actions.add(new KeyboardReleaseAction(strTmp));
                    break;
                case MOUSE_MOVE:
                    intTmp1 = decode(codeArray[i + 1]);
                    intTmp2 = decode(codeArray[i + 2]);
                    i += 3;
                    if (intTmp1 < -10000 || intTmp1 > 10000) break;
                    if (intTmp2 < -10000 || intTmp2 > 10000) break;
                    actions.add(new MouseMoveAction( intTmp1, intTmp2));
                    break;
                case MOUSE_MOVE_AT:
                    intTmp1 = decode(codeArray[i + 1]);
                    intTmp2 = decode(codeArray[i + 2]);
                    i += 3;
                    if (intTmp1 < 0 || intTmp1 > 10000) break;
                    if (intTmp2 < 0 || intTmp2 > 10000) break;
                    actions.add(new MouseMoveAtAction(intTmp1, intTmp2));
                    break;
                case MOUSE_PRESS_LEFT:
                    i++;
                    actions.add(new MousePressLeftAction());
                    break;
                case MOUSE_RELEASE_LEFT:
                    i++;
                    actions.add(new MouseReleaseLeftAction());
                    break;
                case MOUSE_PRESS_RIGHT:
                    i++;
                    actions.add(new MousePressRightAction());
                    break;
                case MOUSE_RELEASE_RIGHT:
                    i++;
                    actions.add(new MouseReleaseRightAction());
                    break;
                case MOUSE_PRESS_MIDDLE:
                    i++;
                    actions.add(new MousePressMiddleAction());
                    break;
                case MOUSE_RELEASE_MIDDLE:
                    i++;
                    actions.add(new MouseReleaseMiddleAction());
                    break;
                case MOUSE_WHEEL_UP:
                    intTmp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (intTmp1 < 0 || intTmp1 > 100) break;
                    actions.add(new MouseWheelUpAction( intTmp1));
                    break;
                case MOUSE_WHEEL_DOWN:
                    intTmp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (intTmp1 < 0 || intTmp1 > 100) break;
                    actions.add(new MouseWheelDownAction( intTmp1));
                    break;
                case DELAY_SECONDS:
                    intTmp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (intTmp1 < 0 || intTmp1 > 3600) break;
                    intTmp1 *= 1000;
                    actions.add(new DelaySecondsAction(intTmp1));
                    break;
                case DELAY_MILLISECONDS:
                    intTmp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (intTmp1 < 0 || intTmp1 > 5000) break;
                    actions.add(new DelayMillisecondsAction(intTmp1));
                    break;
            }
        }
        return actions;
    }

    // decode
    private int decode(char c) {
        return c - SHIFT;
    }

}
