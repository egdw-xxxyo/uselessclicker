package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemCodeGenerator;

import java.util.function.Consumer;

/**
 * system.sleep(232);<br>
 * mouse.buttonAt('LEFT','PRESS',999,330);<br>
 * system.sleep(96);<br>
 * mouse.buttonAt('LEFT','RELEASE',999,330);<br>
 * system.sleep(592);<br>
 * mouse.wheelAt('DOWN',3,1191,353);
 */
public class MouseButtonWheelAtWithDelaysRecorder extends SimpleMouseRecorder implements LupeRequired {
    public MouseButtonWheelAtWithDelaysRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private EventLogger eventLog = new EventLogger(4);

    private CodeGenerator mouseCodeGenerator = new MouseCodeGenerator();
    private CodeGenerator systemCodeGenerator = new SystemCodeGenerator();

    private String code;

    @Override
    public void onStart() {
        super.onStart();

        addListener(new SimpleMouseButtonWheelListener("recording.mouse.buttonWheelAtWithDelays") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();
                code += mouseCodeGenerator.forMethod("buttonAt", event.getButton(), "PRESS", event.getX(), event.getY());
                putString(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();
                code += mouseCodeGenerator.forMethod("buttonAt", event.getButton(), "RELEASE", event.getX(), event.getY());
                putString(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();
                code += mouseCodeGenerator.forMethod("wheelAt", "UP", event.getAmount(), event.getX(), event.getY());
                putString(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();
                code += mouseCodeGenerator.forMethod("wheelAt", "DOWN", event.getAmount(), event.getX(), event.getY());
                putString(code);
            }

            private void forEachEvent() {
                code = "";
                if (eventLog.size() > 1) {
                    int delay = eventLog.getDelay();
                    code += systemCodeGenerator.forMethod("sleep", delay);
                }
            }
        });
    }

    @Override
    protected void onRecordingStarted() {
        eventLog.clear();
    }
}
