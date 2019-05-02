package org.dikhim.jclicker.jsengine.clickauto.objects;


import org.dikhim.clickauto.jsengine.objects.Classes.Image;

public interface SystemObject extends org.dikhim.clickauto.jsengine.objects.SystemObject {
    void exit();

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

    void onMouseMove(String functionName, Object... args);

    void onWheelDown(String functionName, Object... args);

    void onWheelUp(String functionName, Object... args);

    void setMaxThreads(String name, int maxThreads);

    void showImage(Image image);
}
