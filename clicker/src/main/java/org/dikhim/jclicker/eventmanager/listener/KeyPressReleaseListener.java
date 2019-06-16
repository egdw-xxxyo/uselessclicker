package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;

import java.util.function.Consumer;

public class KeyPressReleaseListener extends SimpleListener implements KeyListener {
    private String key;
    private Consumer<KeyPressEvent>  onPress;
    private Consumer<KeyReleaseEvent>  onRelease;

    public KeyPressReleaseListener(String id, String key, Consumer<KeyPressEvent>  onPress, Consumer<KeyReleaseEvent>  onRelease) {
        super(id);
        this.key = key;
        this.onPress = onPress;
        this.onRelease = onRelease;
    }

    @Override
    public void keyPressed(KeyPressEvent event) {
        if(key.isEmpty() || key.toUpperCase().equals("ANY")) onPress.accept(event);
        if (key.toUpperCase().equals(event.getKey())) onPress.accept(event);
    }

    @Override
    public void keyReleased(KeyReleaseEvent event) {
        if(key.isEmpty() || key.toUpperCase().equals("ANY")) onRelease.accept(event);
        if (key.toUpperCase().equals(event.getKey())) onRelease.accept(event);
    }
}
