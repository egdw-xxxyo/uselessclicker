package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;

public class SpecifiedMouseButtonReleaseListener extends SimpleListener implements MouseReleaseListener {
    private final String button;
    private final Runnable onFire;
    public SpecifiedMouseButtonReleaseListener(String id, String button, Runnable onFire) {
        super(id);
        this.button = button.toUpperCase();
        this.onFire = onFire;
    }

    @Override
    public void buttonReleased(MouseReleaseEvent event) {
        if(button.equals(event.getButton()) || button.equals("ANY")) onFire.run();

    }
}
