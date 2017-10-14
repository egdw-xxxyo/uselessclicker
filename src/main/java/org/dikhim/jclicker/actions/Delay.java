package org.dikhim.jclicker.actions;

import java.awt.*;

/**
 * Created by dikobraz on 23.03.17.
 */
public class Delay implements UserAction {
    
    public void play(Robot r) {
        r.delay(ms);
    }

    public int ms;

    public Delay(int ms) {
        this.ms = ms;
    }

    public String toString() {
        return "Deley " + ms + " ms.\n";
    }
}
