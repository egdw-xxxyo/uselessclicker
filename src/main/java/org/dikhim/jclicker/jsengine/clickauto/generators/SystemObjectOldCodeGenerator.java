package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.jsengine.clickauto.objects.SystemObject;

public class SystemObjectOldCodeGenerator extends SimpleOldCodeGenerator implements SystemObject {

    public SystemObjectOldCodeGenerator(int lineSize) {
        super("system", lineSize, SystemObject.class);
    }

    public SystemObjectOldCodeGenerator() {
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
    public double getMultiplier() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public double getSpeed() {
        buildStringForCurrentMethod();
        return 0;
    }

    @Override
    public void keyIgnore() {
        buildStringForCurrentMethod();
    }

    @Override
    public void keyResume() {
        buildStringForCurrentMethod();
    }

    @Override
    public void mouseIgnore() {
        buildStringForCurrentMethod();
    }

    @Override
    public void mouseResume() {
        buildStringForCurrentMethod();
    }

    @Override
    public void onKeyPress(String functionName, String key, Object... args) {
        buildStringForCurrentMethod(functionName, key, args);
    }

    @Override
    public void onKeyRelease(String functionName, String key, Object... args) {
        buildStringForCurrentMethod(functionName, key, args);
    }

    @Override
    public void onShortcutPress(String functionName, String keys, Object... args) {
        buildStringForCurrentMethod(functionName, keys, args);
    }

    @Override
    public void onShortcutRelease(String functionName, String keys, Object... args) {
        buildStringForCurrentMethod(functionName, keys, args);
    }

    @Override
    public void onMousePress(String functionName, String buttons, Object... args) {
        buildStringForCurrentMethod(functionName, buttons, args);
    }

    @Override
    public void onMouseRelease(String functionName, String buttons, Object... args) {
        buildStringForCurrentMethod(functionName, buttons, args);
    }

    @Override
    public void onMouseMove(String functionName, Object... args) {
        buildStringForCurrentMethod(functionName, args);
    }

    @Override
    public void onWheelDown(String functionName, Object... args) {
        buildStringForCurrentMethod(functionName, args);
    }

    @Override
    public void onWheelUp(String functionName, Object... args) {
        buildStringForCurrentMethod(functionName, args);
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
    public void resetMultiplier() {
        buildStringForCurrentMethod();
    }

    @Override
    public void resetSpeed() {
        buildStringForCurrentMethod();
    }

    @Override
    public void setMaxThreads(String name, int maxThreads) {
        buildStringForCurrentMethod(name, maxThreads);
    }

    @Override
    public void setMultiplier(double multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void setSpeed(double multiplier) {
        buildStringForCurrentMethod(multiplier);
    }

    @Override
    public void showImage(Image image) {
        buildStringForCurrentMethod();
    }


    @Override
    public void sleep(int ms) {
        buildStringForCurrentMethod(ms);
    }

    @Override
    public void sleepNonMultiplied(int ms) {
        buildStringForCurrentMethod(ms);
    }
}
