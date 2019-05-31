package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemObjectCodeGenerator;

import java.util.function.Consumer;

public class KeyPerformWithDelaysRecorder extends SimpleRecorder implements KeyRecorder {
    public KeyPerformWithDelaysRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    public void onStart() {
        EventLogger eventLog = new EventLogger(2);
        KeyboardObjectCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator();
        SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator();
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
                putCode(code);
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
                putCode(code);
            }
        });
    }
}
