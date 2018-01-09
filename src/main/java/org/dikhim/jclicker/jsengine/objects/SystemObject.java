package org.dikhim.jclicker.jsengine.objects;

@SuppressWarnings("unused")
public interface SystemObject {
    int getMultipliedDelay(int delay);

    float getMultiplier();

    void print(String s);

    void println(String s);

    void registerShortcut(String shortcut, String function);

    void resetMultiplier();

    void setMultiplier(float multiplier);

    void sleep(int ms);
}
