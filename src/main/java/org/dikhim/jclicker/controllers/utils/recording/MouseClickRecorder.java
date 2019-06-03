package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.MouseButtonEvent;
import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.click('LEFT');<br>
 * mouse.click('RIGHT');
 */
public class MouseClickRecorder extends SimpleMouseRecorder implements MouseRecorder {
    public MouseClickRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventLogger eventLog = new EventLogger(2);
        MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
        addListener("recording.key.performWithDelays", new MouseButtonListener() {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isControlPressed() || eventLog.isEmpty()) return;
                MouseButtonEvent lastMouseButtonEvent = eventLog.getLastMouseButtonEvent();
                if (lastMouseButtonEvent.getButton().equals(event.getButton()) &&
                        lastMouseButtonEvent.getX() >= event.getX() - 2 &&
                        lastMouseButtonEvent.getX() <= event.getX() + 2 &&
                        lastMouseButtonEvent.getY() >= event.getY() - 2 &&
                        lastMouseButtonEvent.getY() <= event.getY() + 2) {
                    mouseObjectCodeGenerator.click(event.getButton());
                    putString(mouseObjectCodeGenerator.getGeneratedCode());
                }
                eventLog.clear();
            }
        });
    }
}
