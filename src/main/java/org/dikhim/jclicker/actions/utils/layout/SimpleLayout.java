package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

import java.util.*;

public class SimpleLayout implements Layout {
    private final String LAYOUT = "us";
    private final String DESCRIPTION = "English(US)";
    private Map<String, List<String>> layoutMap = new HashMap<>();

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
        Optional<Map.Entry<String, List<String>>> entry = layoutMap
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

    /**
     * Puts new character or overrides existed<br>
     * First param - name of a character
     *
     * @param ch - chars
     */
    @Override
    public void put(String... ch) {
        layoutMap.put(ch[0], Arrays.asList(Arrays.copyOfRange(ch, 1, ch.length)));
    }
}
