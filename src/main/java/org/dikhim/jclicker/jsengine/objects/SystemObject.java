package org.dikhim.jclicker.jsengine.objects;

@SuppressWarnings("unused")
public interface SystemObject {
    void print(String s);

    void println(String s);

    void registerShortcut(String shortcut, String function);

    void sleep(int ms);
}
