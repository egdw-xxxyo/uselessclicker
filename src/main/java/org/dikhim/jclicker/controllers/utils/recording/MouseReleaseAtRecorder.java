package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

public class MouseReleaseAtRecorder extends SimpleMouseRecorder{
    public MouseReleaseAtRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.press", new MouseReleaseListener(){
            MouseObjectCodeGenerator codeGenerator = new MouseObjectCodeGenerator();

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isControlPressed()) return;
                codeGenerator.pressAt(event.getButton(),event.getX(), event.getY());
                putCode(codeGenerator.getGeneratedCode());
            }
        });
    }
}
