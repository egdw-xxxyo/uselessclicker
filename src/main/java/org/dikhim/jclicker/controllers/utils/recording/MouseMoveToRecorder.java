package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseButtonListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

public class MouseMoveToRecorder extends SimpleMouseRecorder  {
    public MouseMoveToRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.moveAt", new MouseButtonListener() {
            MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if(!isControlPressed()) return;
                mouseObjectCodeGenerator.moveTo(event.getX(),event.getY());
                putString(mouseObjectCodeGenerator.getGeneratedCode());
            }

            @Override
            public void buttonPressed(MousePressEvent event) {
                
            }
        });
    }
}
