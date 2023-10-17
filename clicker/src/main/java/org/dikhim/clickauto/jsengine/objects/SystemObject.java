package org.dikhim.clickauto.jsengine.objects;


public interface SystemObject {
    int getMultipliedDelay(int delay);

    double getMultiplier();

    double getSpeed();
    
    void print(String s);

    void println();
    
    void println(String s);
    
    void resetMultiplier();

    void resetSpeed();

    void setMultiplier(double multiplier);

    void setSpeed(double multiplier);

    void sleep(int ms);

    void sleepNonMultiplied(int ms);

    long time();

    long nanoTime();
}
