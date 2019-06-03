package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.MouseWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.wheelAt('DOWN',3,562,823);
 */
public class WheelAtRecorder extends SimpleMouseRecorder {
    public WheelAtRecorder(Consumer<String> onRecorded) {
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
                codeGenerator.wheelAt("UP", event.getAmount(), event.getX(), event.getY());
                putString(codeGenerator.getGeneratedCode());
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isControlPressed()) return;
                codeGenerator.wheelAt("DOWN", event.getAmount(), event.getX(), event.getY());
                putString(codeGenerator.getGeneratedCode());
            }
        });
    }
}
