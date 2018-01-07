package org.dikhim.jclicker.jsengine.objects;

@SuppressWarnings("unused")
public interface KeyboardObject {
    // G
    int getMinDelay();

    float getMultiplier();

    int getPressDelay();

    int getReleaseDelay();

    float getSpeed();

    // I
    boolean isPressed(String keys);

    // P
    void perform(String keys, String action);

    void performIgnoringDelays(String keys, String action);

    void press(String keys);

    // R
    void release(String keys);

    void resetDelays();

    void resetMultiplier();

    void resetSpeed();

    // S
    void setDelays(int delay);

    void setMinDelay(int delay);

    void setMultiplier(float multiplier);

    void setPressDelay(int pressDelay);

    void setReleaseDelay(int releaseDelay);

    void setSpeed(float multiplier);

    //T
    void type(String keys);
}
