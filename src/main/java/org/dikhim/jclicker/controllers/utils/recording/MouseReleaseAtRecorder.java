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
                String code = "";
                switch (event.getButton()) {
                    case "LEFT":
                        code = codeGenerator.forMethod("releaseLeftAt", event.getX(), event.getY());
                        break;
                    case "RIGHT":
                        code = codeGenerator.forMethod("releaseRightAt", event.getX(), event.getY());
                        break;
                    case "MIDDLE":
                        code = codeGenerator.forMethod("releaseMiddleAt", event.getX(), event.getY());
                        break;
                }
                putString(code);
            }
        });
    }
}
