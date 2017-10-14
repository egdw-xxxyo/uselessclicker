package org.dikhim.jclicker.actions;

import java.awt.*;

/**
 * Created by dikobraz on 23.03.17.
 */
public class KeyReleased implements UserAction {
    public int keyCode;

    
    public void play(Robot r) {
        r.keyRelease(keyCode);
    }

    public KeyReleased(int keyCode) {
        this.keyCode = keyCode;
    }

    public String toString() {
        return "Key " + keyCode + " released\n";
    }
}
