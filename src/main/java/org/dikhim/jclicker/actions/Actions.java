package org.dikhim.jclicker.actions;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dikobraz on 23.03.17.
 */
public class Actions {
    public ArrayList<UserAction> actions;
    public long currentTime;
    public boolean recording;

    public Actions() {
        actions = new ArrayList<UserAction>();
        currentTime = 0;
        recording = false;
    }

    public void add(UserAction action) {
        if(recording){
            int delay = (int) (new Date().getTime() - currentTime);
            currentTime=new Date().getTime();
            actions.add(new Delay(delay));
            actions.add(action);
        }
    }

    public void clear() {
        actions.clear();
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        for (UserAction ua : actions) {
            sb.append(ua.toString());
        }
        return sb.toString();
    }

    public void startRecording() {
        clear();
        recording = true;
        currentTime = new Date().getTime();
    }

    public void stopRecord() {
        recording = false;
    }

    public void play(Robot r) throws Exception{

        stopRecord();
        for(UserAction ua:actions){
            ua.play(r);
        }
    }
}
