package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.SystemObject;

public class SystemObjectCodeGenerator extends SimpleCodeGenerator implements SystemObject {

    public SystemObjectCodeGenerator(int lineSize) {
        super("system", lineSize);
    }

    @Override
    public int getMultipliedDelay(int delay) {
        buildStringForCurrentMethod(delay);
        return 0;
    }

    public float getMultiplier() {
        buildStringForCurrentMethod();
        return 0;
    }

    public void print(String s) {
        buildStringForCurrentMethod(s);
    }

    public void println(String s) {
        buildStringForCurrentMethod(s);
    }

    public void registerShortcut(String shortcut, String function) {
        buildStringForCurrentMethod(shortcut,function);
    }

    public void resetMultiplier() {
        buildStringForCurrentMethod();
    }

    public void setMultiplier(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    public void sleep(int ms) {
        buildStringForCurrentMethod(ms);
    }
}
