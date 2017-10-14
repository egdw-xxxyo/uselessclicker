package org.dikhim.jclicker.actions;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by dikobraz on 23.03.17.
 */
public class MouseReleased implements UserAction{
    public int keyCode;
    public int x;
    public int y;

    
    public void play(Robot r) {
        if(keyCode==1){
            r.mouseRelease(InputEvent.BUTTON1_MASK);
        }else if(keyCode==2){
            r.mouseRelease(InputEvent.BUTTON2_MASK);
        }else if(keyCode==3){
            r.mouseRelease(InputEvent.BUTTON3_MASK);
        }
    }




    public MouseReleased(int keyCode, int x, int y) {
        this.keyCode = keyCode;
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "Mouse " + keyCode+ " released on: "+x+", "+y + "\n";
    }

}
