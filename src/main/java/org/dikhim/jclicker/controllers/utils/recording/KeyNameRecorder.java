package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.KeyPressListener;

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
        addListener("recording.key.name", (KeyPressListener) e -> putString(e.getKey() + " "));
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecording();
    }
}
