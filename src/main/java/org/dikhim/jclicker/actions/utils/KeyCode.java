package org.dikhim.jclicker.actions.utils;

public class KeyCode {
    private String name;
    private int nativeCode;
    private int eventCode;

    public int getUselessCode() {
        return uselessCode;
    }

    public void setUselessCode(int uselessCode) {
        this.uselessCode = uselessCode;
    }

    private int uselessCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNativeCode() {
        return nativeCode;
    }

    public void setNativeCode(int nativeCode) {
        this.nativeCode = nativeCode;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public KeyCode(String name, int nativeCode, int eventCode, int uselessCode) {
        this.name = name;
        this.nativeCode = nativeCode;
        this.eventCode = eventCode;
        this.uselessCode = uselessCode;
    }
}