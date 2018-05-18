package org.dikhim.jclicker.jsengine.objects.generators;

import org.apache.commons.io.FileUtils;
import org.dikhim.jclicker.jsengine.objects.SystemObject;

public class SystemObjectCodeGenerator extends SimpleCodeGenerator implements SystemObject {

    public SystemObjectCodeGenerator(int lineSize) {
        super("system", lineSize, SystemObject.class);
    }

    public SystemObjectCodeGenerator() {
        super("system", SystemObject.class);
    }


    @Override
    public void exit() {
        buildStringForCurrentMethod();
    }

    @Override
    public int getMultipliedDelay(int delay) {
        buildStringForCurrentMethod(delay);
        return 0;
    }

    @Override
    public float getMultiplier() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public float getSpeed() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public void print(String s) {
        buildStringForCurrentMethod(s);
    }

    @Override
    public void println() {
        buildStringForCurrentMethod();
    }

    @Override
    public void println(String s) {
        buildStringForCurrentMethod(s);
    }

    @Override
    public void registerShortcut(String shortcut, String function) {
        buildStringForCurrentMethod(shortcut, function);
    }

    @Override
    public void resetMultiplier() {
        buildStringForCurrentMethod();
    }

    @Override
    public void resetSpeed() {
        buildStringForCurrentMethod();
    }

    @Override
    public void setMultiplier(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void setSpeed(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void sleep(int ms) {
        buildStringForCurrentMethod(ms);
    }

}
