package org.dikhim.jclicker.ui.util;

import javafx.application.Platform;

public class DoWhilePressed {
    private int initDelay = 200;
    private int repeatDelay = 50;
    private Runnable doWhilePressed = ()->{};
    
    private Thread pressingThread;
    
    public void press(){
        doWhilePressed.run();
        pressingThread = new Thread(() -> {
            try {
                Thread.sleep(initDelay);
                while (true) {
                    Platform.runLater(() -> doWhilePressed.run());
                    Thread.sleep(repeatDelay);
                }
            } catch (InterruptedException ignored) {
            }
        });
        pressingThread.setDaemon(true);
        pressingThread.start();
    }
    
    public void release(){
        pressingThread.interrupt();
    }

    public int getInitDelay() {
        return initDelay;
    }

    public DoWhilePressed setInitDelay(int initDelay) {
        this.initDelay = initDelay;
        return this;
    }

    public int getRepeatDelay() {
        return repeatDelay;
    }

    public DoWhilePressed setRepeatDelay(int repeatDelay) {
        this.repeatDelay = repeatDelay;
        return this;
    }

    public Runnable getDoWhilePressed() {
        return doWhilePressed;
    }

    public DoWhilePressed setDoWhilePressed(Runnable doWhilePressed) {
        this.doWhilePressed = doWhilePressed;
        return this;
    }
}
