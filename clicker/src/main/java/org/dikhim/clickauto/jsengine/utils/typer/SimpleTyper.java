package org.dikhim.clickauto.jsengine.utils.typer;


import org.dikhim.clickauto.jsengine.objects.KeyboardObject;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleTyper implements Typer {
    private Layout layout;
    private KeyboardObject keyboard;

    public SimpleTyper(KeyboardObject keyboardObject, Layout layout) {
        this.keyboard = keyboardObject;
        this.layout = layout;
    }

    @Override
    public void type(String text) {
        List<String> chars = text.chars().mapToObj(e -> Character.toString((char) e)).collect(Collectors.toList());

        for (String c : chars) {
            String key = layout.getKeyFor(c);
            int index = layout.getIndexFor(key, c);
            type(key, index);
        }
        releaseShift();
    }

    private boolean shiftPressed;

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    private void pressShift() {
        if (!isShiftPressed()) {
            shiftPressed = true;
            keyboard.press("SHIFT");
        }
    }

    private void releaseShift() {
        if (isShiftPressed()) {
            shiftPressed = false;
            keyboard.release("SHIFT");
        }
    }

    private void type(String key, int index) {
        if (key.isEmpty() || index < 0) return;
        switch (index) {
            case 0:
                // 0 - type without modifies key
                releaseShift();
                keyboard.type(key);
                break;
            case 1:
                // 1 - type key with SHIFT key pressed
                pressShift();
                keyboard.type(key);
                break;
        }
    }
}
