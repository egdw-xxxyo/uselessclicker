package org.dikhim.clickauto.jsengine.utils;

public class KeyCode {
    private String name;
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

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public KeyCode(String name, int eventCode, int uselessCode) {
        this.name = name;
        this.eventCode = eventCode;
        this.uselessCode = uselessCode;
    }
}