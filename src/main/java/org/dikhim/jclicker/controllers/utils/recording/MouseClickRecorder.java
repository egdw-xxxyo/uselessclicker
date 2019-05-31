package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.MouseButtonEvent;
import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

public class MouseClickRecorder extends SimpleRecorder implements MouseRecorder {
    public MouseClickRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private boolean controlPressed = false;

    @Override
    public void onStart() {
        addListener(
                "recording.key.performWithDelays.controlKey", 
                new KeyPressReleaseListener(
                        "CONTROL", 
                        () -> controlPressed = true, 
                        () -> controlPressed = false));

        EventLogger eventLog = new EventLogger(2);
        MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
        addListener("recording.key.performWithDelays", new MouseButtonListener() {
            @Override
            public void mousePressed(MousePressEvent event) {
                if (!controlPressed) return;
                eventLog.add(event);
            }

            @Override
            public void mouseReleased(MouseReleaseEvent event) {
                if (!controlPressed || eventLog.isEmpty()) return;
                MouseButtonEvent lastMouseButtonEvent = eventLog.getLastMouseButtonEvent();
                if (lastMouseButtonEvent.getButton().equals(event.getButton()) &&
                        lastMouseButtonEvent.getX() >= event.getX() -2 &&
                        lastMouseButtonEvent.getX() <= event.getX() +2 &&
                        lastMouseButtonEvent.getY() >= event.getY() -2 &&
                        lastMouseButtonEvent.getY() <= event.getY() +2) {
                    mouseObjectCodeGenerator.click(event.getButton());
                    putCode(mouseObjectCodeGenerator.getGeneratedCode());
                }
                eventLog.clear();
            }
        });
    }
}
