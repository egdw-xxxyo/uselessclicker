package org.dikhim.clickauto.jsengine.objects;

public interface MouseObject {
    // G
    AnimatedMouse animated();

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

    void moveTo(int x, int y);


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


    void pressLeft();

    void pressRight();

    void pressMiddle();

    void releaseLeft();

    void releaseRight();

    void releaseMiddle();

    void clickLeft();

    void clickRight();

    void clickMiddle();

    void wheelUp(int amount);

    void wheelDown(int amount);

    void pressLeftAt(int x, int y);

    void pressRightAt(int x, int y);

    void pressMiddleAt(int x, int y);

    void releaseLeftAt(int x, int y);

    void releaseRightAt(int x, int y);

    void releaseMiddleAt(int x, int y);

    void clickLeftAt(int x, int y);

    void clickRightAt(int x, int y);

    void clickMiddleAt(int x, int y);

    void wheelUpAt(int x, int y, int amount);

    void wheelDownAt(int x, int y, int amount);

    void moveAndPressLeft(int dx, int dy);

    void moveAndPressRight(int dx, int dy);

    void moveAndPressMiddle(int dx, int dy);

    void moveAndReleaseLeft(int dx, int dy);

    void moveAndReleaseRight(int dx, int dy);

    void moveAndReleaseMiddle(int dx, int dy);

    void moveAndClickLeft(int dx, int dy);

    void moveAndClickRight(int dx, int dy);

    void moveAndClickMiddle(int dx, int dy);

    void moveAndWheelUp(int dx, int dy, int amount);

    void moveAndWheelDown(int dx, int dy, int amount);
}
