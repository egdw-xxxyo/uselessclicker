package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMousePressListener;
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
        addListener(new SimpleMousePressListener("recording.mouse.pressAt") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                String code = "";
                switch (event.getButton()) {
                    case "LEFT":
                        code = codeGenerator.forMethod("pressLeftAt", event.getX(), event.getY());
                        break;
                    case "RIGHT":
                        code = codeGenerator.forMethod("pressRightAt", event.getX(), event.getY());
                        break;
                    case "MIDDLE":
                        code = codeGenerator.forMethod("pressMiddleAt", event.getX(), event.getY());
                        break;
                }
                putString(code);
            }
        });
    }
}
