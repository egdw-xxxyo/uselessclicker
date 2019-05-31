package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemObjectCodeGenerator;

import java.util.function.Consumer;

public class MouseButtonWheelAtRecorder extends SimpleMouseRecorder {
    public MouseButtonWheelAtRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventLogger eventLog = new EventLogger(4);
        MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
        SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator();

        addListener("recording.mouse.buttonWheelAt", new MouseButtonWheelListener() {
            String code;

            @Override
            public void buttonPressed(MousePressEvent event) {
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.buttonAt(event.getButton(), "PRESS", event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putCode(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.buttonAt(event.getButton(), "RELEASE", event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putCode(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.wheelAt("UP", event.getAmount(), event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putCode(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                eventLog.add(event);

                forEachEvent();

                mouseObjectCodeGenerator.wheelAt("DOWN", event.getAmount(), event.getX(), event.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putCode(code);
            }

            private void forEachEvent() {
                if(!isControlPressed()) return;
                code = "";
                if (eventLog.size() > 1) {
                    int delay = eventLog.getDelay();
                    systemObjectCodeGenerator.sleep(delay);
                    code += systemObjectCodeGenerator.getGeneratedCode();
                }
            }
        });
    }
}
