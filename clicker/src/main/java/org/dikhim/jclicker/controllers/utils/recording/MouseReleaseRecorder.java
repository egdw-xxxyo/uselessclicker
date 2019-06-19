package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseReleaseListener;
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
        addListener(new SimpleMouseReleaseListener("recording.mouse.release") {
            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                String code = "";
                switch (event.getButton()) {
                    case "LEFT":
                        code = codeGenerator.forMethod("releaseLeft");
                        break;
                    case "RIGHT":
                        code = codeGenerator.forMethod("releaseRight");
                        break;
                    case "MIDDLE":
                        code = codeGenerator.forMethod("releaseMiddle");
                        break;
                }
                putString(code);
            }
        });
    }
}
