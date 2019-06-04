package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.*;

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
    
    @Override
    public void onStart() {
        startRecording();
        EventLogger eventLog = new EventLogger(2);
        addListener("recording.key.performWithDelays", new KeyListener() {
            String code;

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
