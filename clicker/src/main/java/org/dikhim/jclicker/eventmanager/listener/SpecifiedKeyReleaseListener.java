package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;

public class SpecifiedKeyReleaseListener extends SimpleListener implements KeyReleaseListener {
    private String key;
    private Runnable onFire;

    public SpecifiedKeyReleaseListener(String id, String key, Runnable onFire) {
        super(id);
        this.key = key.toUpperCase();
        this.onFire = onFire;
    }

    @Override
    public void keyReleased(KeyReleaseEvent event) {
        if (key.equals(event.getKey()) || key.equals("ANY")) onFire.run();
    }
}
