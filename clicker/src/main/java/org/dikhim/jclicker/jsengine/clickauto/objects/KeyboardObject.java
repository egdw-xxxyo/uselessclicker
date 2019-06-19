package org.dikhim.jclicker.jsengine.clickauto.objects;

public interface KeyboardObject extends org.dikhim.clickauto.jsengine.objects.KeyboardObject {
    // I
    boolean isPressed(String keys);

    boolean isCapsLocked();

    boolean isNumLocked();

    boolean isScrollLocked();
}
