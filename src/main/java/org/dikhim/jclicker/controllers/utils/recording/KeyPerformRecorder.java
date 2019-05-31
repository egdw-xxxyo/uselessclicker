package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.KeyboardObjectCodeGenerator;

import java.util.function.Consumer;

public class KeyPerformRecorder extends SimpleRecorder implements KeyRecorder{
    public KeyPerformRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        addListener("recording.key.perform", new KeyListener() {
            KeyboardObjectCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator();
            @Override
            public void keyPressed(KeyPressEvent event) {
                keyboardObjectCodeGenerator.perform(event.getKey(), "PRESS");
                putCode(keyboardObjectCodeGenerator.getGeneratedCode());
            }

            @Override
            public void keyReleased(KeyReleaseEvent event) {
                keyboardObjectCodeGenerator.perform(event.getKey(), "RELEASE");
                putCode(keyboardObjectCodeGenerator.getGeneratedCode());
            }
        });
    }
}
