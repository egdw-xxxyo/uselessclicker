package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;
/**
 * mouse.wheel('DOWN',3);
 *
 */
public class WheelRecorder extends SimpleMouseRecorder {
    public WheelRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();
    
    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.press", new MouseWheelListener() {

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;
                putString(codeGenerator.forMethod("wheel","UP", event.getAmount()));
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;
                putString(codeGenerator.forMethod("wheel","DOWN", event.getAmount()));
            }
        });
    }
}
