package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardObjectCodeGenerator extends SimpleCodeGenerator implements KeyboardObject {
    private static Logger LOGGER = LoggerFactory.getLogger(KeyboardObjectCodeGenerator.class);

    public KeyboardObjectCodeGenerator(int lineSize) {
        super("key", lineSize, KeyboardObject.class);
    }

    public KeyboardObjectCodeGenerator() {
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
    public float getMultiplier() {
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
    public float getSpeed() {
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
    public void setMultiplier(float multiplier) {
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
    public void setSpeed(float multiplier) {
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
