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

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;

                putString(codeGenerator.forMethod("wheelUpAt", event.getAmount(), event.getX(), event.getY()));
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;

                putString(codeGenerator.forMethod("wheelDownAt", event.getAmount(), event.getX(), event.getY()));
            }
        });
    }
}
