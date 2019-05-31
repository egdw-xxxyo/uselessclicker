package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;

import java.util.function.Consumer;

public class SimpleMouseRecorder extends SimpleRecorder implements MouseRecorder{
    private boolean controlPressed = false;
    
    public SimpleMouseRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        addListener(
                "recording.mouse.controlKey",
                new KeyPressReleaseListener(
                        "CONTROL",
                        (event) -> {
                            controlPressed = true;
                            controlPressed(event);
                        },
                        (event) -> {
                            controlPressed = false;
                            controlReleased(event);
                        }));
    }

    protected boolean isControlPressed() {
        return controlPressed;
    }

    protected void controlPressed(KeyPressEvent event) {
        
    }

    protected void controlReleased(KeyReleaseEvent event) {

    }
}
