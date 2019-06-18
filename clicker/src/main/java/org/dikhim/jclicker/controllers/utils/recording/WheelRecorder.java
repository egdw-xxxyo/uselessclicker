package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.wheel('DOWN',3);
 */
public class WheelRecorder extends SimpleMouseRecorder {
    public WheelRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();
        addListener(new SimpleMouseWheelListener("recording.mouse.press") {
            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;
                putString(codeGenerator.forMethod("wheelUp", event.getAmount()));
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;
                putString(codeGenerator.forMethod("wheelDown", event.getAmount()));
            }
        });
    }
}
