package org.dikhim.jclicker.jsengine.objects;

@SuppressWarnings("unused")
public interface SystemObject {
    int getMultipliedDelay(int delay);

    float getMultiplier();

    float getSpeed();

    void print(String s);

    void println(String s);

    void registerShortcut(String shortcut, String function);

    void resetMultiplier();

    void resetSpeed();

    void setMultiplier(float multiplier);

    void setSpeed(float multiplier);

    void sleep(int ms);
}
