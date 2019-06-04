package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;

import java.util.function.Consumer;

public class SimpleMouseRecorder extends StringRecorder implements MouseRecorder{
    
    public SimpleMouseRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        addListener(
                "recording.mouse.controlKey",
                new KeyPressReleaseListener(
                        "CONTROL",
                        (event) -> startRecording(),
                        (event) -> stopRecording()));
    }
}
