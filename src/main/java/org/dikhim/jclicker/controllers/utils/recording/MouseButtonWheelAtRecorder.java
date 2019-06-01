package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

public class MouseButtonWheelAtRecorder extends SimpleMouseRecorder {
    public MouseButtonWheelAtRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        super.onStart();
        MouseObjectCodeGenerator codeGenerator = new MouseObjectCodeGenerator();

        addListener("recording.mouse.buttonWheelAt", new MouseButtonWheelListener() {
            

            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isControlPressed()) return;
                codeGenerator.buttonAt(event.getButton(), "PRESS", event.getX(), event.getY());
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isControlPressed()) return;

                codeGenerator.buttonAt(event.getButton(), "RELEASE", event.getX(), event.getY());
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isControlPressed()) return;

                codeGenerator.wheelAt("UP", event.getAmount(), event.getX(), event.getY());
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isControlPressed()) return;

                codeGenerator.wheelAt("DOWN", event.getAmount(), event.getX(), event.getY());
                putString(codeGenerator.getGeneratedCode());
            }
        });
    }
}
