package org.dikhim.clickauto.jsengine.utils.decoders;


import org.dikhim.clickauto.jsengine.actions.*;
import org.dikhim.clickauto.jsengine.utils.KeyCodes;
import org.dikhim.clickauto.jsengine.utils.encoders.NaturalActionEncoder;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("Duplicates")
public class NaturalActionDecoder extends AbstractActionDecoder {
    private NaturalActionEncoder naturalActionEncoder = new NaturalActionEncoder();

    private int i;
    char[] codeArray;

    @Override
    public List<Action> decode(String code) {
        List<Action> actions = new ArrayList<>();

        String stringTmp;
        int intTmp1;
        int intTmp2;

        codeArray = code.toCharArray();
        i = 0;
        while (hasNext()) {
            ActionType actionType = nextActionType();

            if (actionType == null)
                throw new IllegalArgumentException(String.format("incorrect action '%s'", codeArray[i]));

            switch (actionType) {
                case MOUSE_PRESS_LEFT:
                    actions.add(new MousePressLeftAction());
                    break;
                case MOUSE_RELEASE_LEFT:
                    actions.add(new MouseReleaseLeftAction());
                    break;
                case MOUSE_PRESS_RIGHT:
                    actions.add(new MousePressRightAction());
                    break;
                case MOUSE_RELEASE_RIGHT:
                    actions.add(new MouseReleaseRightAction());
                    break;
                case MOUSE_PRESS_MIDDLE:
                    actions.add(new MousePressMiddleAction());
                    break;
                case MOUSE_RELEASE_MIDDLE:
                    actions.add(new MouseReleaseMiddleAction());
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

    private ActionType nextActionType() {
        StringBuilder sb = new StringBuilder();
        while (codeArray[i] == ' ' || codeArray[i] == ';') {
            i++;
        }
        if (!Character.isLetter(codeArray[i]) || codeArray[i] != '_') {
            return null;
        }
        while (Character.isLetter(codeArray[i]) || codeArray[i] == '_') {
            sb.append(codeArray[i]);
            i++;
        }
        return naturalActionEncoder.getActionCodes().getKey(sb.toString());
    }

    private int nextInt() {
        StringBuilder sb = new StringBuilder();

        while (!Character.isDigit(codeArray[i]) || codeArray[i] != '-' ) {
            i++;
        }
        while (Character.isDigit(codeArray[i]) || codeArray[i] == '-' ) {
            sb.append(codeArray[i]);
            i++;
        }
        return Integer.parseInt(sb.toString());

    }

    private String nextString() {
        StringBuilder sb = new StringBuilder();
        while (!Character.isLetter(codeArray[i]) || codeArray[i] != '_') {
            i++;
        }
        while (Character.isLetter(codeArray[i]) || codeArray[i] == '_') {
            sb.append(codeArray[i]);
            i++;
        }
        return sb.toString();
    }
    
    private boolean hasNext() {
        return i < codeArray.length;
    }
    
    // decode
    private int decodeParameter(char c) {
        return c;
    }
}
