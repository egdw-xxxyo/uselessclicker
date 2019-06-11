package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMousePressListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.press('LEFT');
 */
public class MousePressRecorder extends SimpleMouseRecorder {
    public MousePressRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();
        addListener(new SimpleMousePressListener("recording.mouse.press") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                putString(codeGenerator.forMethod("press", event.getButton()));
            }
        });
    }
}
