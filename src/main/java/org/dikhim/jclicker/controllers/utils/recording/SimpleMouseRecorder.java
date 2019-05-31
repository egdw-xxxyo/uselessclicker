package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;

import java.util.function.Consumer;

public class SimpleMouseRecorder extends SimpleRecorder implements MouseRecorder{
    private boolean controlPressed = false;
    
    public SimpleMouseRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    protected void onStart() {
        addListener(
                "recording.mouse.controlKey",
                new KeyPressReleaseListener(
                        "CONTROL",
                        () -> controlPressed = true,
                        () -> controlPressed = false));
    }

    protected boolean isControlPressed() {
        return controlPressed;
    }
}
