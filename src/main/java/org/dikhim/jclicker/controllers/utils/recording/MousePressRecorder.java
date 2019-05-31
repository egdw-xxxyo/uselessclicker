package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.listener.MousePressListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

public class MousePressRecorder extends SimpleMouseRecorder{
    public MousePressRecorder(Consumer<String> onRecorded) {
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
                codeGenerator.press(event.getButton());
                putCode(codeGenerator.getGeneratedCode());
            }
        });
    }
}
