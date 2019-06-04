package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.*;

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
        addListener("recording.mouse.buttonWheelAtWithDelays", new MouseButtonWheelListener() {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                code += mouseCodeGenerator.forMethod("moveAndPress", event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                code += mouseCodeGenerator.forMethod("moveAndRelease", event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                code += mouseCodeGenerator.forMethod("moveAndWheel","UP", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);

                forEachEvent();

                Point p2 = new Point(event.getX(), event.getY());
                code += mouseCodeGenerator.forMethod("moveAndWheel","DOWN", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
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
