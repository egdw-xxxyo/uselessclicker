package org.dikhim.jclicker.actions;

import java.awt.*;

/**
 * Created by dikobraz on 23.03.17.
 */
public class KeyPressed implements UserAction {
    public int keyCode;
    public KeyPressed(int keyCode){
        this.keyCode = keyCode;
    }
    public String toString(){
        return "Key " + keyCode+ " pressed\n";
    }

    
    public void play(Robot r) {
        r.keyPress(keyCode);
    }
}
