package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;
/**
 * mouse.wheel('DOWN',3);
 *
 */
public class WheelRecorder extends SimpleMouseRecorder {
    public WheelRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        super.onStart();
        addListener("recording.mouse.press", new MouseWheelListener() {
            MouseObjectCodeGenerator codeGenerator = new MouseObjectCodeGenerator();

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isControlPressed()) return;
                codeGenerator.wheel("UP", event.getAmount());
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isControlPressed()) return;
                codeGenerator.wheel("DOWN", event.getAmount());
                putString(codeGenerator.getGeneratedCode());
            }
        });
    }
}
