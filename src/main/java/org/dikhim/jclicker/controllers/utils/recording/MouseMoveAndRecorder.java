package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.awt.*;
import java.util.function.Consumer;

public class MouseMoveAndRecorder extends SimpleMouseRecorder {


    public MouseMoveAndRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    Point point1;
    EventLogger eventLog = new EventLogger(4);
    @Override
    public void onStart() {
        super.onStart();
        MouseObjectCodeGenerator codeGenerator = new MouseObjectCodeGenerator();

        addListener("recording.mouse.buttonWheelAt", new MouseButtonWheelListener() {


            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isControlPressed()) return;
                codeGenerator.buttonAt(event.getButton(), "PRESS", event.getX(), event.getY());
                putCode(codeGenerator.getGeneratedCode());
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isControlPressed()) return;

                codeGenerator.buttonAt(event.getButton(), "RELEASE", event.getX(), event.getY());
                putCode(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isControlPressed()) return;

                codeGenerator.wheelAt("UP", event.getAmount(), event.getX(), event.getY());
                putCode(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isControlPressed()) return;

                codeGenerator.wheelAt("DOWN", event.getAmount(), event.getX(), event.getY());
                putCode(codeGenerator.getGeneratedCode());
            }
        });
    }

    @Override
    protected void controlPressed(KeyPressEvent event) {
        super.controlPressed(event);
        point1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }
}
