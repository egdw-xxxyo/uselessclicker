package org.dikhim.jclicker.actions;

import java.awt.*;

/**
 * Created by dikobraz on 23.03.17.
 */
public class MouseMoved implements UserAction {
    public int x;
    public int y;

    public MouseMoved(int x, int y) {
        this.x = x;
        this.y = y;
    }

    
    public void play(Robot r) {
        r.mouseMove(x,y);
    }

    public String toString(){
        return "Mouse moved to: "+x+", "+y + "\n";
    }
}
