package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.MousePressListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.pressAt('LEFT',123,446);
 */
public class MousePressAtRecorder extends SimpleMouseRecorder implements LupeRequired {
    public MousePressAtRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.press", (MousePressListener) event -> {
            if (!isRecording()) return;
            putString(codeGenerator.forMethod("pressAt", event.getButton(), event.getX(), event.getY()));
        });
    }
}
