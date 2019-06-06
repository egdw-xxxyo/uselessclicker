package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleKeyListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.KeyboardCodeGenerator;

import java.util.function.Consumer;

/**
 * key.perform('KEY1','PRESS');<p>
 * key.perform('KEY1','RELEASE');
 */
public class KeyPerformRecorder extends StringRecorder implements KeyRecorder {
    public KeyPerformRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new KeyboardCodeGenerator();

    @Override
    public void onStart() {
        startRecording();
        addListener(new SimpleKeyListener("recording.key.perform") {
            @Override
            public void keyPressed(KeyPressEvent event) {
                putString(codeGenerator.forMethod("perform", event.getKey(), "PRESS"));

            }

            @Override
            public void keyReleased(KeyReleaseEvent event) {
                putString(codeGenerator.forMethod("perform", event.getKey(), "RELEASE"));

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecording();
    }
}
