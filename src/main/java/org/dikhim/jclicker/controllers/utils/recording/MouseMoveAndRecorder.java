package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.awt.*;
import java.util.function.Consumer;

public class MouseMoveAndRecorder extends SimpleMouseRecorder {


    public MouseMoveAndRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    Point p1;

    @Override
    public void onStart() {
        super.onStart();
        MouseObjectCodeGenerator codeGenerator = new MouseObjectCodeGenerator();

        addListener("recording.mouse.buttonWheelAt", new MouseButtonWheelListener() {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isControlPressed()) return;

                Point p2 = new Point(event.getX(), event.getY());
                codeGenerator.moveAndPress(event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isControlPressed()) return;

                Point p2 = new Point(event.getX(), event.getY());
                codeGenerator.moveAndRelease(event.getButton(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isControlPressed()) return;

                Point p2 = new Point(event.getX(), event.getY());
                codeGenerator.moveAndWheel("UP", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isControlPressed()) return;

                Point p2 = new Point(event.getX(), event.getY());
                codeGenerator.moveAndWheel("DOWN", event.getAmount(), p2.x - p1.x, p2.y - p1.y);
                p1 = p2;
                putString(codeGenerator.getGeneratedCode());
            }
        });
    }

    @Override
    protected void controlPressed(KeyPressEvent event) {
        super.controlPressed(event);
        p1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }
}
