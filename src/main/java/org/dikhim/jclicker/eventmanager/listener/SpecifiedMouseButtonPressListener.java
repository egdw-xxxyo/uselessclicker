package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;

public class SpecifiedMouseButtonPressListener extends SimpleListener implements MousePressListener {
    private final String button;
    private final Runnable onFire;
    public SpecifiedMouseButtonPressListener(String id, String button, Runnable onFire) {
        super(id);
        this.button = button.toUpperCase();
        this.onFire = onFire;
    }

    @Override
    public void buttonPressed(MousePressEvent event) {
        if(button.equals(event.getButton()) || button.equals("ANY")) onFire.run();
    }
}
