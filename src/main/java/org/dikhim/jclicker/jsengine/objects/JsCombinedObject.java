package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.actions.actions.*;
import org.dikhim.jclicker.actions.utils.decoders.ActionDecoder;
import org.dikhim.jclicker.actions.utils.decoders.UnicodeDecoder;

import java.util.List;

public class JsCombinedObject implements CombinedObject {
    private MouseObject mouseObject;
    private KeyboardObject keyboardObject;
    private SystemObject systemObject;

    public JsCombinedObject(MouseObject mouseObject, KeyboardObject keyboardObject, SystemObject systemObject) {
        this.mouseObject = mouseObject;
        this.keyboardObject = keyboardObject;
        this.systemObject = systemObject;
    }

    public void run(String code) {
        ActionDecoder actionDecoder = new UnicodeDecoder();
        List<Action> actions = actionDecoder.decode(code);
        long startTime = System.currentTimeMillis();
        int currentTime;
        int scriptTime = 0;

        int delayDiff=0;
        int intTmp;
        for (Action a : actions) {
            switch (a.getType()) {
                case KEYBOARD_PRESS:
                    keyboardObject.performIgnoringDelays(((KeyboardPressAction) a).getKey(), "PRESS");
                    break;
                case KEYBOARD_RELEASE:
                    keyboardObject.performIgnoringDelays(((KeyboardReleaseAction) a).getKey(), "RELEASE");
                    break;
                case MOUSE_MOVE:
                    mouseObject.moveIgnoringDelays(((MouseMoveAction) a).getDx(), ((MouseMoveAction) a).getDy());
                    break;
                case MOUSE_MOVE_AT:
                    mouseObject.moveToIgnoringDelays(((MouseMoveAtAction) a).getX(), ((MouseMoveAtAction) a).getY());
                    break;
                case MOUSE_PRESS_LEFT:
                    mouseObject.buttonIgnoringDelays("LEFT", "PRESS");
                    break;
                case MOUSE_RELEASE_LEFT:
                    mouseObject.buttonIgnoringDelays("LEFT", "RELEASE");
                    break;
                case MOUSE_PRESS_RIGHT:
                    mouseObject.buttonIgnoringDelays("RIGHT", "PRESS");
                    break;
                case MOUSE_RELEASE_RIGHT:
                    mouseObject.buttonIgnoringDelays("RIGHT", "RELEASE");
                    break;
                case MOUSE_PRESS_MIDDLE:
                    mouseObject.buttonIgnoringDelays("MIDDLE", "PRESS");
                    break;
                case MOUSE_RELEASE_MIDDLE:
                    mouseObject.buttonIgnoringDelays("MIDDLE", "RELEASE");
                    break;
                case MOUSE_WHEEL_UP:
                    mouseObject.wheelIgnoringDelays("UP", ((MouseWheelUpAction) a).getAmount());
                    break;
                case MOUSE_WHEEL_DOWN:
                    mouseObject.wheelIgnoringDelays("DOWN", ((MouseWheelDownAction) a).getAmount());
                    break;
                case DELAY_SECONDS:
                    intTmp = ((DelaySecondsAction) a).getDelay() * 1000;
                    systemObject.sleep(intTmp);
                    scriptTime += intTmp;
                    break;
                case DELAY_MILLISECONDS:
                    intTmp = ((DelayMillisecondsAction) a).getDelay();
                    systemObject.sleep(intTmp);
                    scriptTime += intTmp;
                    break;
            }
            currentTime = (int) (System.currentTimeMillis() - startTime);
            delayDiff = currentTime-scriptTime;
        }
    }
}
