package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;
import org.dikhim.jclicker.eventmanager.listener.NonRepeatableSpecifiedKeyPressReleaseListener;

import java.util.function.Consumer;

public class SimpleMouseRecorder extends StringRecorder implements MouseRecorder, MouseControlKeyRequired{
    
    public SimpleMouseRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        addListener(
                new NonRepeatableSpecifiedKeyPressReleaseListener(
                        "recording.mouse.controlKey",
                        Dependency.getConfig().hotKeys().mouseControl().getKeys(),
                        (event) -> startRecording(),
                        (event) -> stopRecording()));
    }
}
