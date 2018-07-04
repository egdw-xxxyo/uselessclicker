package org.dikhim.jclicker.actions.utils.layout;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

public class TyperFactory {
    public static Typer createTyperForLayout(KeyboardObject keyboardObject, String layoutName) throws Exception {
        if ("us".equals(layoutName)) {
            return new UsTyper(keyboardObject);
        } else if ("ru".equals(layoutName)) {
            return new RuTyper(keyboardObject);
        }else if ("ua".equals(layoutName)) {
            return new UaTyper(keyboardObject);
        }
        throw new Exception(String.format("Layout '%s' doesn't supported",layoutName));
    }
}
