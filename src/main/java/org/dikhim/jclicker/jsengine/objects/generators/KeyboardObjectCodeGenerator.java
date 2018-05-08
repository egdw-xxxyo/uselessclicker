package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

public class KeyboardObjectCodeGenerator extends SimpleCodeGenerator implements KeyboardObject {

    public KeyboardObjectCodeGenerator(int lineSize) {
        super("key", lineSize, KeyboardObject.class);
    }

    public KeyboardObjectCodeGenerator() {
        super("key", KeyboardObject.class);
    }


    public int getMinDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getMultipliedPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getMultipliedReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public float getMultiplier() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public int getReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    public float getSpeed() {
        buildStringForCurrentMethod();
        return 0;
    }

    public boolean isPressed(String keys) {
        buildStringForCurrentMethod(keys);
        return false;
    }

    public void perform(String keys, String action) {
        buildStringForCurrentMethod(keys, action);
    }

    public void press(String keys) {
        buildStringForCurrentMethod(keys);
    }

    public void release(String keys) {
        buildStringForCurrentMethod(keys);
    }

    public void resetDelays() {
        buildStringForCurrentMethod();
    }

    public void resetMultiplier() {
        buildStringForCurrentMethod();
    }

    public void resetSpeed() {
        buildStringForCurrentMethod();
    }

    public void setDelays(int delay) {
        buildStringForCurrentMethod(delay);
    }

    public void setMinDelay(int delay) {
        buildStringForCurrentMethod(delay);
    }

    public void setMultiplier(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    public void setPressDelay(int pressDelay) {
        buildStringForCurrentMethod(pressDelay);
    }

    public void setReleaseDelay(int releaseDelay) {
        buildStringForCurrentMethod(releaseDelay);
    }

    public void setSpeed(float multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    public void type(String keys) {
        buildStringForCurrentMethod(keys);

    }
}
