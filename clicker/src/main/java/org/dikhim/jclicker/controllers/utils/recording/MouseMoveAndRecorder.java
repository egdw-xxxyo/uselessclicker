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
                String code = "";
                switch (event.getButton()) {
                    case "LEFT":
                        code = mouseCodeGenerator.forMethod("moveAndPressLeft", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "RIGHT":
                        code = mouseCodeGenerator.forMethod("moveAndPressRight", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "MIDDLE":
                        code = mouseCodeGenerator.forMethod("moveAndPressMiddle", p2.x - p1.x, p2.y - p1.y);
                        break;
                }
                p1 = p2;
                putString(code);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;

                Point p2 = new Point(event.getX(), event.getY());
                String code = "";
                switch (event.getButton()) {
                    case "LEFT":
                        code = mouseCodeGenerator.forMethod("moveAndReleaseLeft", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "RIGHT":
                        code = mouseCodeGenerator.forMethod("moveAndReleaseRight", p2.x - p1.x, p2.y - p1.y);
                        break;
                    case "MIDDLE":
                        code = mouseCodeGenerator.forMethod("moveAndReleaseMiddle", p2.x - p1.x, p2.y - p1.y);
                        break;
                }
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;

                Point p2 = new Point(event.getX(), event.getY());
                String code = mouseCodeGenerator.forMethod("moveAndWheelUp", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(code);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;

                Point p2 = new Point(event.getX(), event.getY());
                String code = mouseCodeGenerator.forMethod("moveAndWheelDown", event.getAmount(), p2.x - p1.x, p2.y - p1.y);

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
