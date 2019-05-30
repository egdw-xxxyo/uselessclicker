package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.KeyPressListener;

import java.util.function.Consumer;

public class KeyNameRecorder extends SimpleRecorder {
    public KeyNameRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        addListener("recording.key.name", (KeyPressListener) e -> putCode(e.getKey() + " "));
    }

    @Override
    public void onStop() {
        removeListener("recording.key.name");
    }
}
