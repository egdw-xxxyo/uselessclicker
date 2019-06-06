package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleKeyPressListener;

import java.util.function.Consumer;

/**
 * 'KEY1 KEY2 '
 */
public class KeyNameRecorder extends StringRecorder implements KeyRecorder {
    public KeyNameRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        startRecording();
        addListener(new SimpleKeyPressListener("recording.key.name") {
            @Override
            public void keyPressed(KeyPressEvent event) {
                putString(event.getKey() + " ");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecording();
    }
}
