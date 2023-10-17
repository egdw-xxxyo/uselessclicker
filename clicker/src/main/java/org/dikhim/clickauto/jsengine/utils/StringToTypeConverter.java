package org.dikhim.clickauto.jsengine.utils;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.dikhim.clickauto.jsengine.actions.Action;
import org.dikhim.clickauto.jsengine.actions.KeyboardPressAction;
import org.dikhim.clickauto.jsengine.actions.KeyboardReleaseAction;

import java.util.ArrayList;
import java.util.List;

public class StringToTypeConverter {
    BidiMap<String, String> chars = new DualHashBidiMap<>();
    {
        chars.put("a", "A");
        chars.put("b", "B");
        chars.put("c", "C");
        chars.put("d", "D");
        chars.put("e", "E");
        chars.put("f", "F");
        chars.put("g", "G");
        chars.put("h", "H");
        chars.put("i", "I");
        chars.put("j", "J");
        chars.put("k", "K");
        chars.put("l", "L");
        chars.put("m", "M");
        chars.put("n", "N");
        chars.put("o", "O");
        chars.put("p", "P");
        chars.put("q", "Q");
        chars.put("r", "R");
        chars.put("s", "S");
        chars.put("t", "T");
        chars.put("u", "U");
        chars.put("v", "V");
        chars.put("w", "W");
        chars.put("x", "X");
        chars.put("y", "Y");
        chars.put("z", "Z");
        chars.put("0", ")");
        chars.put("1", "!");
        chars.put("2", "@");
        chars.put("3", "#");
        chars.put("4", "$");
        chars.put("5", "%");
        chars.put("6", "^");
        chars.put("7", "&");
        chars.put("8", "*");
        chars.put("9", "(");
        chars.put("`", "~");
        chars.put("-", "_");
        chars.put("=", "+");
        chars.put("[", "{");
        chars.put("]", "}");
        chars.put("\\", "|");
        chars.put(",", "<");
        chars.put(".", ">");
        chars.put("/", "?");
        chars.put(";", ":");
        chars.put("'", "\"");
        chars.put(" ", " ");
    }
    public StringToTypeConverter() {
    }

    public List<String> getListOfEventsToType(String text) {
        return null;
    }

    private boolean capsLocked = false;

    public void setCapsLocked(boolean capsLocked) {
        this.capsLocked = capsLocked;
    }

    public boolean isCapsLocked() {
        return capsLocked;
    }

    private boolean shiftPressed = false;

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
    }

    public void text(String text) {
        char[] chars = text.toCharArray();
        for (char c : chars) {
            addChar(c);
        }
    }

    private boolean isKey(char c) {
        return chars.containsKey(Character.toString(c));
    }

    private boolean isValue(char c) {
        return chars.containsValue(Character.toString(c));
    }

    List<Action> actions = new ArrayList<>();

    private void addChar(char c) {
        if (isKey(c)) {
            // обычный символ
            if (isCapsLocked()) {
                // если включен caps lock
                // то зажать клавишу shift
                if (Character.isLetter(c)) {
                    pressShift();
                }
                type(c);
            } else {
                // если caps lock выключен
                releaseShift();
                type(c);
            } 
        } else if (isValue(c)) {
            // с зажатым шифтом
            if (isCapsLocked()) {
                // если включен caps lock
                // то зажать клавишу shift
                if (Character.isLetter(c)) {
                    releaseShift();
                }
                type(c);
            } else {
                // если caps lock выключен
                pressShift();
                type(c);
            }
        }
    }
    
    private void pressShift() {
        if (!isShiftPressed()) {
            setShiftPressed(true);
            System.out.println("press:shift");

            actions.add(new KeyboardPressAction("SHIFT"));
        }
    }

    private void releaseShift() {
        if (isShiftPressed()) {
            setShiftPressed(false);
            System.out.println("release:shift");
            actions.add(new KeyboardReleaseAction("SHIFT"));
        }
    }

    private void type(char c) {
        String keyName = "";
        if (c == ' ') {
            keyName = "SPACE";
        } else {
            if (isValue(c)) {
                keyName = chars.getKey(Character.toString(c));
                keyName = keyName.toUpperCase();
            }else if(isKey(c)) {
                keyName = Character.toString(c);
            }
        }
        System.out.println("type:"+keyName);
        actions.add(new KeyboardPressAction(keyName));
        actions.add(new KeyboardReleaseAction(keyName));
    }
    
    public void typeText(String layout, String text){
        
    }

}
