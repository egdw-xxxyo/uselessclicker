package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemObjectCodeGenerator;

import java.util.function.Consumer;

/**
 * system.sleep(232);<br>
 * mouse.buttonAt('LEFT','PRESS',999,330);<br>
 * system.sleep(96);<br>
 * mouse.buttonAt('LEFT','RELEASE',999,330);<br>
 * system.sleep(592);<br>
 * mouse.wheelAt('DOWN',3,1191,353);
 */
public class MouseButtonWheelAtWithDelaysRecorder extends SimpleMouseRecorder {
    public MouseButtonWheelAtWithDelaysRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    EventLogger eventLog = new EventLogger(4);

    @Override
    public void onStart() {
        super.onStart();
        MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
        SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator();

        addListener("recording.mouse.buttonWheelAt", new MouseButtonWheelListener() {
            String code;

            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.buttonAt(event.getButton(), "PRESS", event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.buttonAt(event.getButton(), "RELEASE", event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.wheelAt("UP", event.getAmount(), event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.wheelAt("DOWN", event.getAmount(), event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            private void forEachEvent() {
                code = "";
                if (eventLog.size() > 1) {
                    int delay = eventLog.getDelay();
                    systemObjectCodeGenerator.sleep(delay);
                    code += systemObjectCodeGenerator.getGeneratedCode();
                }
            }
        });
    }

    @Override
    protected void controlPressed(KeyPressEvent event) {
        super.controlPressed(event);
        eventLog.clear();
    }
}
