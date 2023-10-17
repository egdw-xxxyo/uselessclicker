package org.dikhim.clickauto.jsengine.utils.decoders;


import org.dikhim.clickauto.jsengine.actions.*;
import org.dikhim.clickauto.jsengine.utils.KeyCodes;
import org.dikhim.clickauto.jsengine.utils.encoders.Base64ActionEncoder;
import org.dikhim.clickauto.jsengine.utils.encoding.Base64Decoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Base64ActionDecoder extends AbstractActionDecoder {
    private Base64Decoder decoder = new Base64Decoder();
    private Base64ActionEncoder base64ActionEncoder = new Base64ActionEncoder();


    @Override

    public List<Action> decode(String code) {
        List<Action> actions = new ArrayList<>();
        int paramLength = 3;
        String stringTmp;
        int intTmp1;
        int intTmp2;

        char[] codeArray = code.toCharArray();
        int i = 0;
        while (i < codeArray.length) {
            ActionType actionType = decodeActionType(codeArray[i]);

            if (actionType == null)
                throw new IllegalArgumentException(String.format("incorrect action '%s'", codeArray[i]));
            i++;
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

                    stringTmp = KeyCodes.getNameByUselessCode(getParam(codeArray, i, paramLength));
                    if (!validateKey(stringTmp))
                        throw new IllegalArgumentException(
                                String.format("incorrect key '%s'", codeArray[i]));
                    actions.add(new KeyboardPressAction(stringTmp));
                    i += paramLength;
                    break;
                case KEYBOARD_RELEASE:
                    stringTmp = KeyCodes.getNameByUselessCode(getParam(codeArray, i, paramLength));
                    if (!validateKey(stringTmp))
                        throw new IllegalArgumentException(
                                String.format("incorrect key '%s'", codeArray[i]));
                    actions.add(new KeyboardReleaseAction(stringTmp));
                    i += paramLength;
                    break;
                case MOUSE_WHEEL_UP:
                    intTmp1 = getParam(codeArray, i, paramLength);
                    if (!validateWheelAmount(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect wheel amount '%s' -> '%s'", codeArray[i], intTmp1));
                    actions.add(new MouseWheelUpAction(intTmp1));
                    i += paramLength;
                    break;
                case MOUSE_WHEEL_DOWN:
                    intTmp1 = getParam(codeArray, i, paramLength);
                    if (!validateWheelAmount(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect wheel amount '%s' -> '%s'", codeArray[i], intTmp1));
                    actions.add(new MouseWheelDownAction(intTmp1));
                    i += paramLength;
                    break;
                case DELAY_SECONDS:
                    intTmp1 = getParam(codeArray, i, 3);
                    if (!validateDelaySeconds(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect second delay '%s' -> '%s'", codeArray[i], intTmp1));
                    actions.add(new DelaySecondsAction(intTmp1));
                    i += paramLength;
                    break;
                case DELAY_MILLISECONDS:
                    intTmp1 = getParam(codeArray, i, paramLength);
                    if (!validateDelayMilliseconds(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect millisecond delay '%s' -> '%s'", codeArray[i], intTmp1));
                    actions.add(new DelayMillisecondsAction(intTmp1));
                    i += paramLength;
                    break;
                case MOUSE_MOVE_TO:
                    intTmp1 = getParam(codeArray, i, paramLength);

                    if (!validateCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect x coordinate '%s' -> '%s'", codeArray[i], intTmp1));
                    i += paramLength;

                    intTmp2 = getParam(codeArray, i, paramLength);
                    if (!validateCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect y coordinate '%s' -> '%s'", codeArray[i], intTmp1));
                    actions.add(new MouseMoveToAction(intTmp1, intTmp2));
                    i += paramLength;
                    break;
                case MOUSE_MOVE:
                    intTmp1 = getParam(codeArray, i, paramLength);

                    if (!validateDifferenceCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect dx coordinate '%s' -> '%s'", codeArray[i], intTmp1));
                    i += paramLength;

                    intTmp2 = getParam(codeArray, i, paramLength);
                    if (!validateDifferenceCoordinate(intTmp1))
                        throw new IllegalArgumentException(
                                String.format("incorrect dy coordinate '%s' -> '%s'", codeArray[i], intTmp1));
                    actions.add(new MouseMoveAction(intTmp1, intTmp2));
                    i += paramLength;
                    break;
            }
        }
        return actions;
    }

    private ActionType decodeActionType(char actionTypeChar) {
        return base64ActionEncoder.getActionCodes().getKey(Character.toString(actionTypeChar));
    }

    // decode
    private int decodeParameter(char[] chars) {
        return decoder.decode(chars);
    }

    private int getParam(char[] chars, int begin, int length) {
        return decodeParameter(Arrays.copyOfRange(chars, begin, begin + length));
    }

}
