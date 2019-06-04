package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyboardObjectOldCodeGenerator extends SimpleOldCodeGenerator implements KeyboardObject {
    private static Logger LOGGER = LoggerFactory.getLogger(KeyboardObjectOldCodeGenerator.class);

    public KeyboardObjectOldCodeGenerator(int lineSize) {
        super("key", lineSize, KeyboardObject.class);
    }

    public KeyboardObjectOldCodeGenerator() {
        super("key", KeyboardObject.class);
    }

    @Override
    public int getMinDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getMultipliedPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getMultipliedReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public double getMultiplier() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getPressDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public int getReleaseDelay() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public double getSpeed() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public boolean isPressed(String keys) {
        buildStringForCurrentMethod(keys);
        return false;
    }

    @Override
    public boolean isCapsLocked() {
        buildStringForCurrentMethod();
        return false;
    }

    @Override
    public boolean isNumLocked() {
        buildStringForCurrentMethod();
        return false;
    }

    @Override
    public boolean isScrollLocked() {
        buildStringForCurrentMethod();
        return false;
    }

    @Override
    public void perform(String keys, String action) {
        buildStringForCurrentMethod(keys, action);
    }

    @Override
    public void press(String keys) {
        buildStringForCurrentMethod(keys);
    }

    @Override
    public void release(String keys) {
        buildStringForCurrentMethod(keys);
    }

    @Override
    public void resetDelays() {
        buildStringForCurrentMethod();
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
    public void setDelays(int delay) {
        buildStringForCurrentMethod(delay);
    }

    @Override
    public void setMinDelay(int delay) {
        buildStringForCurrentMethod(delay);
    }

    @Override
    public void setMultiplier(double multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void setPressDelay(int pressDelay) {
        buildStringForCurrentMethod(pressDelay);
    }

    @Override
    public void setReleaseDelay(int releaseDelay) {
        buildStringForCurrentMethod(releaseDelay);
    }

    @Override
    public void setSpeed(double multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void type(String keys) {
        buildStringForCurrentMethod(keys);
    }

    @Override
    public void typeText(String layout, String text) {
        buildStringForCurrentMethod(layout, text);
    }
}
