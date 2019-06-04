package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectOldCodeGenerator;

import java.util.function.Consumer;


/**
 * mouse.releaseAt('LEFT',838,450);
 */
public class MouseReleaseAtRecorder extends SimpleMouseRecorder implements LupeRequired {
    public MouseReleaseAtRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.press", (MouseReleaseListener) event -> {
            if (!isRecording()) return;
            putString(codeGenerator.forMethod("pressAt", event.getButton(), event.getX(), event.getY()));
        });
    }
}
