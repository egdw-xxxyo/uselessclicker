package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.MouseButtonEvent;
import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseButtonListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.click('LEFT');<br>
 * mouse.click('RIGHT');
 */
public class MouseClickRecorder extends SimpleMouseRecorder implements MouseRecorder {
    public MouseClickRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator mouseCodeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();
        EventLogger eventLog = new EventLogger(2);
        addListener(new SimpleMouseButtonListener("recording.mouse.click") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording() || eventLog.isEmpty()) return;
                MouseButtonEvent lastMouseButtonEvent = eventLog.getLastMouseButtonEvent();
                if (lastMouseButtonEvent.getButton().equals(event.getButton()) &&
                        lastMouseButtonEvent.getX() >= event.getX() - 2 &&
                        lastMouseButtonEvent.getX() <= event.getX() + 2 &&
                        lastMouseButtonEvent.getY() >= event.getY() - 2 &&
                        lastMouseButtonEvent.getY() <= event.getY() + 2) {
                    String code = "";
                    switch (event.getButton()) {
                        case "LEFT":
                            code = mouseCodeGenerator.forMethod("clickLeft");
                            break;
                        case "RIGHT":
                            code = mouseCodeGenerator.forMethod("clickRight");
                            break;
                        case "MIDDLE":
                            code = mouseCodeGenerator.forMethod("clickMiddle");
                            break;
                    }
                    putString(code);
                }
                eventLog.clear();
            }
        });
    }
}
