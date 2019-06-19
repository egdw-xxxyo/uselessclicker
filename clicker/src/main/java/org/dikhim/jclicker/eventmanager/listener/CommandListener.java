package org.dikhim.jclicker.eventmanager.listener;

import org.dikhim.jclicker.actions.KeyboardListener;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.util.LimitedSizeQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandListener extends SimpleListener implements KeyListener {
    private final List<String> command;
    private final LimitedSizeQueue<String> typedKeys;
    private final Runnable onFire;
    private final PressedKeyHolder pressedKeyHolder = new PressedKeyHolder();

    private String lastPressed = "";

    public CommandListener(String id, String command, Runnable onFire) {
        super(id);
        this.command = Arrays.asList(command.split(" "));
        this.typedKeys = new LimitedSizeQueue<>(this.command.size());
        this.onFire = onFire;
    }

    @Override
    public void keyPressed(KeyPressEvent event) {
        String key = event.getKey();
        pressedKeyHolder.press(key);
        if (lastPressed.equals(key)) return;
        lastPressed = key;
    }

    @Override
    public void keyReleased(KeyReleaseEvent event) {
        String key = event.getKey();
        pressedKeyHolder.release(key);

        if (lastPressed.equals(key) && pressedKeyHolder.getPressedKeys().size() == 0) {
            typedKeys.add(key);
            check();
        }
    }

    private void check() {
        if (typedKeys.equals(command)) {
            typedKeys.clear();
            onFire.run();
        }
    }
}
