package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.awt.*;
import java.util.function.Consumer;

public class MouseMoveAndRecorder extends SimpleMouseRecorder implements LupeRequired {


    public MouseMoveAndRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private Point p1;
    private CodeGenerator mouseCodeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();

        addListener(new SimpleMouseButtonWheelListener("recording.mouse.moveAnd") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;

                Point p2 = new Point(event.getX(), event.getY());
                String code = mouseCodeGenerator.forMethod("moveAndPress", event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;

                Point p2 = new Point(event.getX(), event.getY());
                String code = mouseCodeGenerator.forMethod("moveAndRelease", event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;

                Point p2 = new Point(event.getX(), event.getY());
                String code = mouseCodeGenerator.forMethod("moveAndWheel", "UP", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;

                Point p2 = new Point(event.getX(), event.getY());
                String code = mouseCodeGenerator.forMethod("moveAndWheel", "DOWN", event.getAmount(), p2.x - p1.x, p2.y - p1.y);

                p1 = p2;
                putString(code);
            }
        });
    }

    @Override
    protected void onRecordingStarted() {
        p1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }
}
