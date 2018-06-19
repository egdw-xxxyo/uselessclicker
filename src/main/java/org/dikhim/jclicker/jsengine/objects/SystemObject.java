package org.dikhim.jclicker.jsengine.objects;

@SuppressWarnings("unused")
public interface SystemObject {
    void exit();
    
    int getMultipliedDelay(int delay);

    float getMultiplier();

    float getSpeed();
    
    void onKeyPress(String functionName, Object ... args);

    void onKeyRelease(String functionName, Object ... args);

    void onMousePress(String functionName, Object ... args);

    void onMouseRelease(String functionName, Object ... args);
    
    void onMouseMove(String functionName, Object ... args);
    
    void onWheelDown(String functionName, Object ... args);
    
    void onWheelUp(String functionName, Object ... args);
    
    void print(String s);

    void println();
    
    void println(String s);

    void registerShortcut(String shortcut, String function);

    void resetMultiplier();

    void resetSpeed();

    void setMultiplier(float multiplier);

    void setSpeed(float multiplier);

    void sleep(int ms);
}
