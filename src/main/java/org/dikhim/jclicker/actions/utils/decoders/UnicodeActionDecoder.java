package org.dikhim.jclicker.actions.utils.decoders;

import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.utils.encoders.AbstractActionEncoder;
import org.dikhim.jclicker.actions.utils.encoders.UnicodeActionEncoder;
import org.dikhim.jclicker.util.output.Out;

import static org.dikhim.jclicker.actions.utils.encoders.UnicodeActionEncoder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnicodeActionDecoder implements ActionDecoder {


    public List<Action> decode(String code) {
        List<Action> actions = new ArrayList<>();

        String stringTmp;
        int intTmp1;
        int intTmp2;

        char[] codeArray = code.toCharArray();
        int i = 0;
        while (i < codeArray.length) {
            ActionType actionType = decodeActionType(Character.toString(codeArray[i]));

            if (actionType == null)
                throw new IllegalArgumentException(String.format("incorrect action '%s'", codeArray[i]));
            
            switch (actionType) {
                case MOUSE_PRESS_LEFT:
                    actions.add(new MousePressLeftAction());
                    i++;
                    break;
                case MOUSE_RELEASE_LEFT:
                    actions.add(new MouseReleaseLeftAction());
                    i++;
                    break;
                case MOUSE_PRESS_RIGHT:
                    actions.add(new MousePressRightAction());
                    i++;
                    break;
                case MOUSE_RELEASE_RIGHT:
                    actions.add(new MouseReleaseRightAction());
                    i++;
                    break;
                case MOUSE_PRESS_MIDDLE:
                    actions.add(new MousePressMiddleAction());
                    i++;
                    break;
                case MOUSE_RELEASE_MIDDLE:
                    actions.add(new MouseReleaseMiddleAction());
                    i++;
                    break;
                case KEYBOARD_PRESS:
                    stringTmp = KeyCodes.getNameByUselessCode(decodeParameter(codeArray[i + 1]));
                    if (!validateKey(stringTmp))
                        throw new IllegalArgumentException(
                                String.format("incorrect key '%s'", codeArray[i + 1]));

                    actions.add(new KeyboardPressAction(stringTmp));
                    i += 2;
                    break;
                case KEYBOARD_RELEASE:
                    stringTmp = KeyCodes.getNameByUselessCode(decodeParameter(codeArray[i + 1]));
                    if (!validateKey(stringTmp))
                        throw new IllegalArgumentException(
                                String.format("incorrect key '%s'", codeArray[i + 1]));
                    actions.add(new KeyboardReleaseAction(stringTmp));
                    i += 2;
                    break;
                case MOUSE_WHEEL_UP:
                    intTmp1 = decodeParameter(codeArray[i + 1]);
                    if (!validateWheelAmount(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect wheel amount '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    actions.add(new MouseWheelUpAction(intTmp1));
                    i += 2;
                    break;
                case MOUSE_WHEEL_DOWN:
                    intTmp1 = decodeParameter(codeArray[i + 1]);
                    if (!validateWheelAmount(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect wheel amount '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    actions.add(new MouseWheelDownAction(intTmp1));
                    i += 2;
                    break;
                case DELAY_SECONDS:
                    intTmp1 = decodeParameter(codeArray[i + 1]);
                    if (!validateDelaySeconds(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect second delay '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    actions.add(new DelaySecondsAction(intTmp1));
                    i += 2;
                    break;
                case DELAY_MILLISECONDS:
                    intTmp1 = decodeParameter(codeArray[i + 1]);
                    if (!validateDelayMilliseconds(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect millisecond delay '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    actions.add(new DelayMillisecondsAction(intTmp1));
                    i += 2;
                    break;
                case MOUSE_MOVE_TO:
                    intTmp1 = decodeParameter(codeArray[i + 1]);
                    if (!validateCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect x coordinate '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    intTmp2 = decodeParameter(codeArray[i + 2]);
                    if (!validateCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect y coordinate '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    actions.add(new MouseMoveToAction(intTmp1, intTmp2));
                    i += 3;
                    break;
                case MOUSE_MOVE:
                    intTmp1 = decodeParameter(codeArray[i + 1]);
                    if (!validateCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect dx coordinate '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    intTmp2 = decodeParameter(codeArray[i + 2]);
                    if (!validateCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect dy coordinate '%s' -> '%s'", codeArray[i + 1], intTmp1));
                    actions.add(new MouseMoveAction(intTmp1, intTmp2));
                    i += 3;
                    break;
            }
        }
        return actions;
    }

    private ActionType decodeActionType(String actionType) {
        return getActionCodes().getKey(actionType);
    }

    // decode
    private int decodeParameter(char c) {
        return c - SHIFT;
    }

    private boolean validateKey(String key) {
        return !key.isEmpty();
    }

    private boolean validateDelaySeconds(int value) {
        return value >= 0 && value <= MAX_DELAY_SECONDS;
    }

    private boolean validateDelayMilliseconds(int value) {
        return value >= 0 && value <= MAX_DELAY_MILLISECONDS;
    }

    private boolean validateCoordinate(int value) {
        return value >= 0 && value <= MAX_COORDINATE;
    }

    private boolean validateDifferenceCoordinate(int value) {
        return value >= MIN_D_COORDINATE && value <= MAX_D_COORDINATE;
    }

    private boolean validateWheelAmount(int value) {
        return value >= 0 && value < MAX_WHEEL_AMOUNT;
    }
}
