package org.dikhim.jclicker.actions.utils.decoders;

import org.apache.commons.collections4.BidiMap;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.utils.MouseCodes;
import org.dikhim.jclicker.actions.utils.encoders.ActionType;
import org.dikhim.jclicker.actions.utils.encoders.UnicodeEncoder;

import java.awt.*;

public class UnicodeDecoder implements ActionDecoder {

    private BidiMap<ActionType, Character> actionCodes = UnicodeEncoder.getActionCodes();
    private final int SHIFT = UnicodeEncoder.getSHIFT();

    public void run(Robot robot, String code) {
        char[] codeArray = code.toCharArray();
        int i = 0;
        while (i < codeArray.length) {
            char c = codeArray[i];
            ActionType actionType = actionCodes.getKey(c);

            int temp1, temp2;
            switch (actionType) {
                case KEYBOARD_PRESS:
                    temp1 = KeyCodes.getEventCodeByUselessCode(decode(codeArray[i + 1]));
                    i += 2;
                    if (temp1 != -1) robot.keyPress(temp1);
                    break;
                case KEYBOARD_RELEASE:
                    temp1 = KeyCodes.getEventCodeByUselessCode(decode(codeArray[i + 1]));
                    i += 2;
                    if (temp1 != -1) robot.keyRelease(temp1);
                    break;
                case MOUSE_MOVE:
                    temp1 = decode(codeArray[i + 1]);
                    temp2 = decode(codeArray[i + 2]);
                    i += 3;
                    if (temp1 < -10000 || temp1 > 10000) break;
                    if (temp2 < -10000 || temp2 > 10000) break;
                    temp1 += MouseEventsManager.getInstance().getX();
                    temp2 += MouseEventsManager.getInstance().getY();
                    robot.mouseMove(temp1, temp2);
                    break;
                case MOUSE_MOVE_AT:
                    temp1 = decode(codeArray[i + 1]);
                    temp2 = decode(codeArray[i + 2]);
                    i += 3;
                    if (temp1 < 0 || temp1 > 10000) break;
                    if (temp2 < 0 || temp2 > 10000) break;
                    robot.mouseMove(temp1, temp2);
                    break;
                case MOUSE_PRESS_LEFT:
                    i++;
                    robot.mousePress(MouseCodes.getEventCodeByName("LEFT"));
                    break;
                case MOUSE_RELEASE_LEFT:
                    i++;
                    robot.mouseRelease(MouseCodes.getEventCodeByName("LEFT"));
                    break;
                case MOUSE_PRESS_RIGHT:
                    i++;
                    robot.mousePress(MouseCodes.getEventCodeByName("RIGHT"));
                    break;
                case MOUSE_RELEASE_RIGHT:
                    i++;
                    robot.mouseRelease(MouseCodes.getEventCodeByName("RIGHT"));
                    break;
                case MOUSE_PRESS_MIDDLE:
                    i++;
                    robot.mousePress(MouseCodes.getEventCodeByName("MIDDLE"));
                    break;
                case MOUSE_RELEASE_MIDDLE:
                    i++;
                    robot.mouseRelease(MouseCodes.getEventCodeByName("MIDDLE"));
                    break;
                case MOUSE_WHEEL_UP:
                    temp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (temp1 < 0 || temp1 > 100) break;
                    temp1 *= -1;
                    robot.mouseWheel(temp1);
                    break;
                case MOUSE_WHEEL_DOWN:
                    temp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (temp1 < 0 || temp1 > 100) break;
                    robot.mouseWheel(temp1);
                    break;
                case DELAY_SECONDS:
                    temp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (temp1 < 0 || temp1 > 3600) break;
                    temp1 += 1000;
                    robot.delay(temp1);
                    break;
                case DELAY_MILISECONDS:
                    temp1 = decode(codeArray[i + 1]);
                    i += 2;
                    if (temp1 < 0 || temp1 > 5000) break;
                    robot.delay(temp1);
                    break;
            }
        }
    }

    private int decode(char c) {
        return c - SHIFT;
    }


}
