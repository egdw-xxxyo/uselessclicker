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

import java.awt.*;
import java.util.function.Consumer;

/**
 * mouse.moveAndButton('LEFT',PRESS,-2,0);<br>
 * mouse.moveAndButton('LEFT',RELEASE,94,-27);<br>
 * mouse.moveAndWheel('DOWN',3,0,0);<br>
 * mouse.moveAndWheel('DOWN',3,0,0);<br>
 * mouse.moveAndWheel('DOWN',3,-13,22);
 */
public class MouseMoveAndWithDelaysRecorder extends SimpleMouseRecorder implements LupeRequired {
    public MouseMoveAndWithDelaysRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private String code = "";
    private Point p1;
    private EventLogger eventLog = new EventLogger(4);
    private CodeGenerator mouseCodeGenerator = new MouseCodeGenerator();
    private CodeGenerator systemCodeGenerator = new SystemCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();
        addListener(new SimpleMouseButtonWheelListener("recording.mouse.buttonWheelAtWithDelays") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                switch (event.getButton()) {
                    case "LEFT":
                        code += mouseCodeGenerator.forMethod("moveAndPressLeft", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "RIGHT":
                        code += mouseCodeGenerator.forMethod("moveAndPressRight", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "MIDDLE":
                        code += mouseCodeGenerator.forMethod("moveAndPressMiddle", p2.x - p1.x, p2.y - p1.y);
                        break;
                }
                p1 = p2;
                putString(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                switch (event.getButton()) {
                    case "LEFT":
                        code += mouseCodeGenerator.forMethod("moveAndReleaseLeft", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "RIGHT":
                        code += mouseCodeGenerator.forMethod("moveAndReleaseRight", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "MIDDLE":
                        code += mouseCodeGenerator.forMethod("moveAndReleaseMiddle", p2.x - p1.x, p2.y - p1.y);
                        break;
                }
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                code += mouseCodeGenerator.forMethod("moveAndWheelUp", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                code += mouseCodeGenerator.forMethod("moveAndWheelDown", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
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
        p1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }
}
