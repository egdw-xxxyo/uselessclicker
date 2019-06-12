package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.event.MousePressEvent;
import org.dikhim.jclicker.eventmanager.event.MouseReleaseEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleMouseButtonWheelListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.util.function.Consumer;

/**
 * mouse.buttonAt('LEFT','PRESS',1004,353);<br>
 * mouse.buttonAt('LEFT','RELEASE',1004,353);<br>
 * mouse.wheelAt('DOWN',3,1111,344);<br>
 * mouse.wheelAt('DOWN',3,1211,348);
 */
public class MouseButtonWheelAtRecorder extends SimpleMouseRecorder implements LupeRequired {
    public MouseButtonWheelAtRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator codeGenerator = new MouseCodeGenerator();

    @Override
    public void onStart() {
        super.onStart();

        addListener(new SimpleMouseButtonWheelListener("recording.mouse.buttonWheelAt") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                
                putString(codeGenerator.forMethod("buttonAt", event.getButton(), "PRESS", event.getX(), event.getY()));
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                putString(codeGenerator.forMethod("buttonAt", event.getButton(), "RELEASE", event.getX(), event.getY()));
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;

                putString(codeGenerator.forMethod("wheelAt", "UP", event.getAmount(), event.getX(), event.getY()));
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;

                putString(codeGenerator.forMethod("wheelAt", "DOWN", event.getAmount(), event.getX(), event.getY()));
            }
        });
    }
}
