package org.dikhim.jclicker.jsengine.objects;

public class ComputerObject {
    private MouseObject mouseObject;
    private KeyboardObject keyboardObject;
    private SystemObject systemObject;

    public ComputerObject(KeyboardObject keyboardObject, MouseObject mouseObject, SystemObject systemObject) {
        this.mouseObject = mouseObject;
        this.keyboardObject = keyboardObject;
        this.systemObject = systemObject;
    }

    public MouseObject getMouseObject() {
        return mouseObject;
    }

    public void setMouseObject(MouseObject mouseObject) {
        this.mouseObject = mouseObject;
    }

    public KeyboardObject getKeyboardObject() {
        return keyboardObject;
    }

    public void setKeyboardObject(KeyboardObject keyboardObject) {
        this.keyboardObject = keyboardObject;
    }

    public SystemObject getSystemObject() {
        return systemObject;
    }

    public void setSystemObject(SystemObject systemObject) {
        this.systemObject = systemObject;
    }
}
