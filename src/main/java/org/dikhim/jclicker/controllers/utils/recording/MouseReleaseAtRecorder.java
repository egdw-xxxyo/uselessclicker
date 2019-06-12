package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

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
        addListener(new SimpleMouseReleaseListener("recording.mouse.releaseAt") {
            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                putString(codeGenerator.forMethod("releaseAt", event.getButton(), event.getX(), event.getY()));
            }
        });
    }
}
