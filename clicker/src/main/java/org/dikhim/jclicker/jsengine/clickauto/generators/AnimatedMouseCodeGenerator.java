package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.clickauto.jsengine.objects.AnimatedMouse;

public class AnimatedMouseCodeGenerator extends SimpleCodeGenerator {
    public AnimatedMouseCodeGenerator(int lineSize) {
        super("mouse.animated", AnimatedMouse.class, lineSize);
    }

    public AnimatedMouseCodeGenerator() {
        super("mouse.animated", AnimatedMouse.class);
    }
}
