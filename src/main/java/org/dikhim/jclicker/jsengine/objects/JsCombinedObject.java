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
        // for delay compensation
        long firstTimeStamp = System.currentTimeMillis();

        int currentTime;
        int scriptTime = 0;
        int delayDiff = 0;

        int actionDelay;
        int calculatedDelay;

        for (Action a : actions) {
            switch (a.getType()) {
                case KEYBOARD_PRESS:
                    keyboardObject.perform(((KeyboardPressAction) a).getKey(), "PRESS");
                    break;
                case KEYBOARD_RELEASE:
                    keyboardObject.perform(((KeyboardReleaseAction) a).getKey(), "RELEASE");
                    break;
                case MOUSE_MOVE:
                    mouseObject.move(((MouseMoveAction) a).getDx(), ((MouseMoveAction) a).getDy());
                    break;
                case MOUSE_MOVE_TO:
                    mouseObject.moveTo(((MouseMoveAtAction) a).getX(), ((MouseMoveAtAction) a).getY());
                    break;
                case MOUSE_PRESS_LEFT:
                    mouseObject.button("LEFT", "PRESS");
                    break;
                case MOUSE_RELEASE_LEFT:
                    mouseObject.button("LEFT", "RELEASE");
                    break;
                case MOUSE_PRESS_RIGHT:
                    mouseObject.button("RIGHT", "PRESS");
                    break;
                case MOUSE_RELEASE_RIGHT:
                    mouseObject.button("RIGHT", "RELEASE");
                    break;
                case MOUSE_PRESS_MIDDLE:
                    mouseObject.button("MIDDLE", "PRESS");
                    break;
                case MOUSE_RELEASE_MIDDLE:
                    mouseObject.button("MIDDLE", "RELEASE");
                    break;
                case MOUSE_WHEEL_UP:
                    mouseObject.wheel("UP", ((MouseWheelUpAction) a).getAmount());
                    break;
                case MOUSE_WHEEL_DOWN:
                    mouseObject.wheel("DOWN", ((MouseWheelDownAction) a).getAmount());
                    break;
                case DELAY_SECONDS:
                    actionDelay = ((DelaySecondsAction) a).getDelay() * 1000;
                    calculatedDelay = actionDelay - delayDiff;
                    if (calculatedDelay < 0) calculatedDelay = 0;
                    systemObject.sleep(calculatedDelay);
                    scriptTime += systemObject.getMultipliedDelay(actionDelay);
                    break;
                case DELAY_MILLISECONDS:
                    actionDelay = ((DelayMillisecondsAction) a).getDelay();
                    calculatedDelay = actionDelay - delayDiff;
                    if (calculatedDelay < 0) calculatedDelay = 0;
                    systemObject.sleep(calculatedDelay);
                    scriptTime += systemObject.getMultipliedDelay(actionDelay);
                    break;
            }
            currentTime = (int) (System.currentTimeMillis() - firstTimeStamp);
            delayDiff = currentTime - scriptTime;
            if(delayDiff>100) delayDiff = 100;
        }
    }
}
