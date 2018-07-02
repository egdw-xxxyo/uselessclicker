package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsLayout implements Layout {
    private final String LAYOUT = "us";
    private final String DESCRIPTION = "English(US)";
    private Map<String, List<String>> keys;

    {
        keys = new HashMap<>();
        keys.put("A", Arrays.asList("a", "A"));
        keys.put("B", Arrays.asList("b", "B"));
        keys.put("C", Arrays.asList("c", "C"));
        keys.put("D", Arrays.asList("d", "D"));
        keys.put("E", Arrays.asList("e", "E"));
        keys.put("F", Arrays.asList("f", "F"));
        keys.put("G", Arrays.asList("g", "G"));
        keys.put("H", Arrays.asList("h", "H"));
        keys.put("I", Arrays.asList("i", "I"));
        keys.put("J", Arrays.asList("j", "J"));
        keys.put("K", Arrays.asList("k", "K"));
        keys.put("L", Arrays.asList("l", "L"));
        keys.put("M", Arrays.asList("m", "M"));
        keys.put("N", Arrays.asList("n", "N"));
        keys.put("O", Arrays.asList("o", "O"));
        keys.put("P", Arrays.asList("p", "P"));
        keys.put("Q", Arrays.asList("q", "Q"));
        keys.put("R", Arrays.asList("r", "R"));
        keys.put("S", Arrays.asList("s", "S"));
        keys.put("T", Arrays.asList("t", "T"));
        keys.put("U", Arrays.asList("u", "U"));
        keys.put("V", Arrays.asList("v", "V"));
        keys.put("W", Arrays.asList("w", "W"));
        keys.put("X", Arrays.asList("x", "X"));
        keys.put("Y", Arrays.asList("y", "Y"));
        keys.put("Z", Arrays.asList("z", "Z"));
        keys.put("0", Arrays.asList("0", ")"));
        keys.put("1", Arrays.asList("1", "!"));
        keys.put("2", Arrays.asList("2", "@"));
        keys.put("3", Arrays.asList("3", "#"));
        keys.put("4", Arrays.asList("4", "$"));
        keys.put("5", Arrays.asList("5", "%"));
        keys.put("6", Arrays.asList("6", "^"));
        keys.put("7", Arrays.asList("7", "&"));
        keys.put("8", Arrays.asList("8", "*"));
        keys.put("9", Arrays.asList("9", "("));
        keys.put("`", Arrays.asList("`", "~"));
        keys.put("-", Arrays.asList("-", "_"));
        keys.put("=", Arrays.asList("=", "+"));
        keys.put("[", Arrays.asList("[", "{"));
        keys.put("]", Arrays.asList("]", "}"));
        keys.put("\\", Arrays.asList("\\", "|"));
        keys.put(",", Arrays.asList(",", "<"));
        keys.put(".", Arrays.asList(".", ">"));
        keys.put("/", Arrays.asList("/", "?"));
        keys.put(";", Arrays.asList(";", ":"));
        keys.put("'", Arrays.asList("'", "\""));
        keys.put(" ", Arrays.asList(" ", " "));
    }

    @Override
    public void type(KeyboardObject keyboardObject, String text) {

    }

    @Override
    public String getLayout() {
        return LAYOUT;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public Map<String, List<String>> getKeys() {
        return keys;
    }
    
    
}
