package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

import java.util.*;

public class UsLayout implements Layout {
    private final String LAYOUT = "us";
    private final String DESCRIPTION = "English(US)";
    private Map<String, List<String>> layoutMap;

    {
        layoutMap = new HashMap<>();
        layoutMap.put("A", Arrays.asList("a", "A"));
        layoutMap.put("B", Arrays.asList("b", "B"));
        layoutMap.put("C", Arrays.asList("c", "C"));
        layoutMap.put("D", Arrays.asList("d", "D"));
        layoutMap.put("E", Arrays.asList("e", "E"));
        layoutMap.put("F", Arrays.asList("f", "F"));
        layoutMap.put("G", Arrays.asList("g", "G"));
        layoutMap.put("H", Arrays.asList("h", "H"));
        layoutMap.put("I", Arrays.asList("i", "I"));
        layoutMap.put("J", Arrays.asList("j", "J"));
        layoutMap.put("K", Arrays.asList("k", "K"));
        layoutMap.put("L", Arrays.asList("l", "L"));
        layoutMap.put("M", Arrays.asList("m", "M"));
        layoutMap.put("N", Arrays.asList("n", "N"));
        layoutMap.put("O", Arrays.asList("o", "O"));
        layoutMap.put("P", Arrays.asList("p", "P"));
        layoutMap.put("Q", Arrays.asList("q", "Q"));
        layoutMap.put("R", Arrays.asList("r", "R"));
        layoutMap.put("S", Arrays.asList("s", "S"));
        layoutMap.put("T", Arrays.asList("t", "T"));
        layoutMap.put("U", Arrays.asList("u", "U"));
        layoutMap.put("V", Arrays.asList("v", "V"));
        layoutMap.put("W", Arrays.asList("w", "W"));
        layoutMap.put("X", Arrays.asList("x", "X"));
        layoutMap.put("Y", Arrays.asList("y", "Y"));
        layoutMap.put("Z", Arrays.asList("z", "Z"));
        layoutMap.put("0", Arrays.asList("0", ")"));
        layoutMap.put("1", Arrays.asList("1", "!"));
        layoutMap.put("2", Arrays.asList("2", "@"));
        layoutMap.put("3", Arrays.asList("3", "#"));
        layoutMap.put("4", Arrays.asList("4", "$"));
        layoutMap.put("5", Arrays.asList("5", "%"));
        layoutMap.put("6", Arrays.asList("6", "^"));
        layoutMap.put("7", Arrays.asList("7", "&"));
        layoutMap.put("8", Arrays.asList("8", "*"));
        layoutMap.put("9", Arrays.asList("9", "("));
        layoutMap.put("`", Arrays.asList("`", "~"));
        layoutMap.put("-", Arrays.asList("-", "_"));
        layoutMap.put("=", Arrays.asList("=", "+"));
        layoutMap.put("[", Arrays.asList("[", "{"));
        layoutMap.put("]", Arrays.asList("]", "}"));
        layoutMap.put("\\", Arrays.asList("\\", "|"));
        layoutMap.put(",", Arrays.asList(",", "<"));
        layoutMap.put(".", Arrays.asList(".", ">"));
        layoutMap.put("/", Arrays.asList("/", "?"));
        layoutMap.put(";", Arrays.asList(";", ":"));
        layoutMap.put("'", Arrays.asList("'", "\""));
        layoutMap.put("SPACE", Arrays.asList(" ", " "));
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
    public Map<String, List<String>> getLayoutMap() {
        return layoutMap;
    }

    @Override
    public String getKeyFor(String character) {
        Optional<Map.Entry<String,List<String>>> entry  =  layoutMap
                .entrySet()
                .stream()
                .filter(
                        stringListEntry -> stringListEntry
                                .getValue()
                                .contains(character))
                .findFirst();
        if (entry.isPresent()) {
            return entry.get().getKey();
        } else {
            return "";
        } 
    }
    
    @Override
    public int getIndexFor(String key, String character) {
        List<String> characters = layoutMap.get(key);
        return characters.indexOf(character);
    }
}
