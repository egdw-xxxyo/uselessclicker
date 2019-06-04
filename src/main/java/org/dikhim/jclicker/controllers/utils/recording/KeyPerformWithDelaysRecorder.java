package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.KeyboardObjectOldCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemObjectOldCodeGenerator;

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

    @Override
    public void onStart() {
        startRecording();
        EventLogger eventLog = new EventLogger(2);
        KeyboardObjectOldCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectOldCodeGenerator();
        SystemObjectOldCodeGenerator systemObjectCodeGenerator = new SystemObjectOldCodeGenerator();
        addListener("recording.key.performWithDelays", new KeyListener() {
            String code;

            @Override
            public void keyPressed(KeyPressEvent event) {
                eventLog.add(event);

                if (eventLog.size() > 1) {
                    systemObjectCodeGenerator.sleep(eventLog.getDelay());
                    code = systemObjectCodeGenerator.getGeneratedCode();
                }

                keyboardObjectCodeGenerator.perform(event.getKey(), "PRESS");
                code += keyboardObjectCodeGenerator.getGeneratedCode();
                putString(code);
            }

            @Override
            public void keyReleased(KeyReleaseEvent event) {
                eventLog.add(event);

                if (eventLog.size() > 1) {
                    systemObjectCodeGenerator.sleep(eventLog.getDelay());
                    code = systemObjectCodeGenerator.getGeneratedCode();
                }

                keyboardObjectCodeGenerator.perform(event.getKey(), "RELEASE");
                code += keyboardObjectCodeGenerator.getGeneratedCode();
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
