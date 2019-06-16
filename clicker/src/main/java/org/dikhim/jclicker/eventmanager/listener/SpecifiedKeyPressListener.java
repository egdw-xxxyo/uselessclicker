package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;

public class SpecifiedKeyPressListener extends SimpleListener implements KeyPressListener {
    private String key;
    private Runnable onFire;

    public SpecifiedKeyPressListener(String id, String key, Runnable onFire) {
        super(id);
        this.key = key.toUpperCase();
        this.onFire = onFire;
    }
    
    @Override
    public void keyPressed(KeyPressEvent event) {
        if (key.equals(event.getKey()) || key.equals("ANY")) onFire.run();
    }
}
