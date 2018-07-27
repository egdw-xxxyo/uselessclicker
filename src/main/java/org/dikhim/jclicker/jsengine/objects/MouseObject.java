package org.dikhim.jclicker.jsengine.objects;

@SuppressWarnings("unused")
public interface MouseObject {
    // B
    void button(String button, String action);

    void buttonAt(String button, String action, int x, int y);

    // C
    void click(String button);

    void clickAt(String button, int x, int y);

    // G
    int getMinDelay();

    int getMoveDelay();

    int getMultipliedMoveDelay();

    int getMultipliedPressDelay();

    int getMultipliedReleaseDelay();

    int getMultipliedWheelDelay();

    double getMultiplier();

    int getPressDelay();

    int getReleaseDelay();

    double getSpeed();

    int getWheelDelay();

    int getX();

    int getY();


    // M
    void move(int dx, int dy);

    void moveAndButton(String button, String action, int dx, int dy);

    void moveAndClick(String button, int dx, int dy);

    void moveAndPress(String button, int dx, int dy);

    void moveAndRelease(String button, int dx, int dy);

    void moveAndWheel(String direction, int amount, int dx, int dy);

    void moveTo(int x, int y);

    // P
    void press(String button);

    void pressAt(String button, int x, int y);

    // R
    void release(String button);

    void releaseAt(String button, int x, int y);

    void resetDelays();

    void resetMultiplier();

    void resetSpeed();

    // S
    void setDelays(int delay);

    void setMinDelay(int minDelay);

    void setMoveDelay(int moveDelay);

    void setMultiplier(double multiplier);

    void setPressDelay(int pressDelay);

    void setReleaseDelay(int releaseDelay);

    void setSpeed(double multiplier);

    void setWheelDelay(int wheelDelay);

    void setX(int x);

    void setY(int y);

    // W
    void wheel(String direction, int amount);

    void wheelAt(String direction, int amount, int x, int y);
}
