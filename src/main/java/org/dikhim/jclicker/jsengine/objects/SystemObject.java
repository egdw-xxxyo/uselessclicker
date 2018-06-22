package org.dikhim.jclicker.jsengine.objects;

@SuppressWarnings("unused")
public interface SystemObject {
    void exit();
    
    int getMultipliedDelay(int delay);

    float getMultiplier();

    float getSpeed();
    
    void keyIgnore();
    
    void keyResume();
    
    void mouseIgnore();

    void mouseResume();
    
    void onKeyPress(String functionName, String key, Object... args);
    
    void onShortcutPress(String functionName, String keys, Object... args);
    
    void onShortcutRelease(String functionName, String keys, Object... args);
    
    void onKeyRelease(String functionName, String key, Object... args);

    void onMousePress(String functionName, String buttons, Object... args);

    void onMouseRelease(String functionName, String buttons, Object... args);
    
    void onMouseMove(String functionName, Object ... args);
    
    void onWheelDown(String functionName, Object ... args);
    
    void onWheelUp(String functionName, Object ... args);
    
    void print(String s);

    void println();
    
    void println(String s);

    void setMaxThreads(String name, int maxThreads);
    
    void resetMultiplier();

    void resetSpeed();

    void setMultiplier(float multiplier);

    void setSpeed(float multiplier);

    void sleep(int ms);
}
