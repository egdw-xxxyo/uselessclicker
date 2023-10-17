package org.dikhim.clickauto.jsengine.objects;

public interface KeyboardObject {
    // G
    int getMinDelay();

    int getMultipliedPressDelay();

    int getMultipliedReleaseDelay();

    double getMultiplier();

    int getPressDelay();

    int getReleaseDelay();

    double getSpeed();

    // P
    void perform(String keys, String action);

    void press(String keys);

    // R
    void release(String keys);

    void resetDelays();

    void resetMultiplier();

    void resetSpeed();

    // S
    void setDelays(int delay);

    void setMinDelay(int delay);

    void setMultiplier(double multiplier);

    void setPressDelay(int pressDelay);

    void setReleaseDelay(int releaseDelay);

    void setSpeed(double multiplier);

    //T
    void type(String keys);

    void typeText(String layout, String text);
}
