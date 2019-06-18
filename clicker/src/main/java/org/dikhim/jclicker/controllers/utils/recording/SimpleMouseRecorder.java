package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;

import java.util.function.Consumer;

public class SimpleMouseRecorder extends StringRecorder implements MouseRecorder, MouseControlKeyRequired{
    
    public SimpleMouseRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        addListener(
                new KeyPressReleaseListener(
                        "recording.mouse.controlKey",
                        "CONTROL",
                        (event) -> startRecording(),
                        (event) -> stopRecording()));
    }
}
