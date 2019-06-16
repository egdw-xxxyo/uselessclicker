package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleKeyboardListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.KeyboardCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemCodeGenerator;

import java.util.function.Consumer;

/**
 * key.perform('KEY1','PRESS');<br>
 * system.sleep(100);<br>
 * key.perform('KEY1','RELEASE');<br>
 * system.sleep(100);
 */
public class KeyPerformWithDelaysRecorder extends StringRecorder implements KeyRecorder {
    public KeyPerformWithDelaysRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CodeGenerator keyboardCodeGenerator = new KeyboardCodeGenerator();
    private CodeGenerator systemCodeGenerator = new SystemCodeGenerator();
    private String code;
    private EventLogger eventLog = new EventLogger(2);

    @Override
    public void onStart() {
        startRecording();
        addListener(new SimpleKeyboardListener("recording.key.performWithDelays") {
            @Override
            public void keyPressed(KeyPressEvent event) {
                eventLog.add(event);

                if (eventLog.size() > 1) {
                    code = systemCodeGenerator.forMethod("sleep", eventLog.getDelay());
                }

                code += keyboardCodeGenerator.forMethod("perform", event.getKey(), "PRESS");
                putString(code);
            }

            @Override
            public void keyReleased(KeyReleaseEvent event) {
                eventLog.add(event);

                if (eventLog.size() > 1) {
                    code = systemCodeGenerator.forMethod("sleep", eventLog.getDelay());
                }

                code += keyboardCodeGenerator.forMethod("perform", event.getKey(), "RELEASE");
                putString(code);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecording();
    }
}
