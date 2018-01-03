package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.KeyboardObject;

public class KeyboardObjectCodeGenerator extends SimpleCodeGenerator implements KeyboardObject {
    public KeyboardObjectCodeGenerator(String objectName, int lineSize) {
        super(objectName, lineSize);
    }

    public float getMultiplier() {
        begin().append("();\n");
        return 0;
    }

    public int getPressDelay() {
        begin().append("();\n");
        return 0;
    }

    public int getReleaseDelay() {
        begin().append("();\n");
        return 0;
    }

    public float getSpeed() {
        begin().append("();\n");
        return 0;
    }

    public boolean isPressed(String keys) {
        begin().append("('")
                .append(keys).append("');\n");
        return false;
    }

    public void perform(String keys, String action) {
        begin().append("('")
                .append(keys).append("','")
                .append(action).append("');\n");
    }

    public void press(String keys) {
        begin().append("('")
                .append(keys).append("');\n");
    }

    public void release(String keys) {
        begin().append("('")
                .append(keys).append("');\n");
    }

    public void resetDelays() {
        begin().append("();\n");
    }

    public void resetMultiplier() {
        begin().append("();\n");
    }

    public void resetSpeed() {
        begin().append("();\n");
    }

    public void setDelays(int delay) {
        begin().append("(")
                .append(delay).append(");\n");
    }

    public void setMultiplier(float multiplier) {
        begin().append("(")
                .append(multiplier).append(");\n");
    }

    public void setPressDelay(int pressDelay) {
        begin().append("(")
                .append(pressDelay).append(");\n");
    }

    public void setReleaseDelay(int releaseDelay) {
        begin().append("(")
                .append(releaseDelay).append(");\n");
    }

    public void setSpeed(float multiplier) {
        begin().append("(")
                .append(multiplier).append(");\n");
    }

    public void type(String keys) {
        begin().append("('")
                .append(keys).append("');\n");
    }
}
