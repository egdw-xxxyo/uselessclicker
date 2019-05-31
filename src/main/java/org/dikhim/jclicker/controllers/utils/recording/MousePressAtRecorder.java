package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.listener.MousePressListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

public class MousePressAtRecorder extends SimpleMouseRecorder{
    public MousePressAtRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.press", new MousePressListener(){
            MouseObjectCodeGenerator codeGenerator = new MouseObjectCodeGenerator();

            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isControlPressed()) return;
                codeGenerator.pressAt(event.getButton(), event.getX(), event.getY());
                putCode(codeGenerator.getGeneratedCode());
            }
        });
    }
}
