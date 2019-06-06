package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseReleaseListener;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseReleaseListener;

import java.util.function.Consumer;

/**
 * 'LEFT RIGHT MIDDLE '
 */
public class MouseNameRecorder extends SimpleMouseRecorder {
    public MouseNameRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        super.onStart();
        addListener(new SimpleMouseReleaseListener("recording.mouse.name") {
            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (isRecording()) putString(event.getButton() + " ");

            }
        });
    }
}
