package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemObjectCodeGenerator;

import java.awt.*;
import java.util.function.Consumer;

public class MouseMoveAndWithDelaysRecorder extends SimpleMouseRecorder {


    public MouseMoveAndWithDelaysRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }
    String code = "";
    Point p1;
    EventLogger eventLog = new EventLogger(4);

    @Override
    public void onStart() {
        super.onStart();
        MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
        SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator();
        addListener("recording.mouse.buttonWheelAt", new MouseButtonWheelListener() {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();
                
                Point p2 = new Point(event.getX(), event.getY());
                mouseObjectCodeGenerator.moveAndPress(event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                mouseObjectCodeGenerator.moveAndRelease(event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                mouseObjectCodeGenerator.moveAndWheel("UP", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isControlPressed()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                mouseObjectCodeGenerator.moveAndWheel("DOWN", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
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
        p1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }
}
