package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.moveAt(888,651);
 */
public class MouseMoveToRecorder extends SimpleMouseRecorder implements LupeRequired {
    public MouseMoveToRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();
        addListener(new SimpleMouseReleaseListener("recording.mouse.moveTo") {
                        @Override
                        public void buttonReleased(MouseReleaseEvent event) {
                            if (!isRecording()) return;
                            putString(codeGenerator.forMethod("moveTo", event.getX(), event.getY()));
                        }
                    });
    }
}
