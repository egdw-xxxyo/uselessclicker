package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.MouseReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.release('LEFT');
 */
public class MouseReleaseRecorder extends SimpleMouseRecorder {
    public MouseReleaseRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();
    
    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.press", (MouseReleaseListener) event -> {
            if (!isRecording()) return;
            putString(codeGenerator.forMethod("release",event.getButton()));
        });
    }
}
