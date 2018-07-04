package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

public class TyperFactory {
    public static Typer createTyperForLayout(KeyboardObject keyboardObject, String layoutName) throws Exception {
        if ("us".equals(layoutName)) {
            return new UsTyper(keyboardObject);
        }
        throw new Exception(String.format("Layout '%s' doesn't supported",layoutName));
    }
}
