package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.KeyboardObjectOldCodeGenerator;

import java.util.function.Consumer;

/**
 * key.perform('KEY1','PRESS');<p>
 * key.perform('KEY1','RELEASE');
 */
public class KeyPerformRecorder extends StringRecorder implements KeyRecorder {
    public KeyPerformRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        startRecording();
        addListener("recording.key.perform", new KeyListener() {
            KeyboardObjectOldCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectOldCodeGenerator();

            @Override
            public void keyPressed(KeyPressEvent event) {
                keyboardObjectCodeGenerator.perform(event.getKey(), "PRESS");
                putString(keyboardObjectCodeGenerator.getGeneratedCode());
            }

            @Override
            public void keyReleased(KeyReleaseEvent event) {
                keyboardObjectCodeGenerator.perform(event.getKey(), "RELEASE");
                putString(keyboardObjectCodeGenerator.getGeneratedCode());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecording();
    }

}
