package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.KeyPressListener;

import java.util.function.Consumer;

public class KeyNameRecorder extends SimpleRecorder implements KeyRecorder{
    public KeyNameRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        addListener("recording.key.name", (KeyPressListener) e -> putCode(e.getKey() + " "));
    }
}
