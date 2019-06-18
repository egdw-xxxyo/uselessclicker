package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.clickauto.jsengine.utils.encoders.ActionEncoder;
import org.dikhim.clickauto.jsengine.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.configuration.storage.CombinedRecordingParams;
import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.listener.*;
import org.dikhim.jclicker.jsengine.clickauto.EventsConverter;
import org.dikhim.jclicker.jsengine.clickauto.generators.CombinedCodeGenerator;

import java.util.function.Consumer;

/**
 * combined.run("......
 */
public class CombinedRecorder extends StringRecorder implements MouseRecorder, KeyRecorder, LupeRequired, CombinedControlKeyRequiered {
    public CombinedRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CombinedCodeGenerator combinedCodeGenerator;
    private String control;
    private EventLogger eventLog;
    private boolean ignoreNextKeyRelease = false;

    @Override
    public void onStart() {
        combinedCodeGenerator = new CombinedCodeGenerator();
        control = Dependency.getConfig().hotKeys().combinedControl().getKeys();
        eventLog = new EventLogger();
        // control button listener. Start on release and stop after press
        addListener(new KeyPressReleaseListener("recording.combined.control", control,
                event -> {
                    if (isRecording()) {
                        stopRecording();
                        ignoreNextKeyRelease = true;
                    }
                },
                event -> {
                    if (ignoreNextKeyRelease) {
                        ignoreNextKeyRelease = false;
                        return;
                    }
                    if (!isRecording() && !ignoreNextKeyRelease) {
                        startRecording();
                    }

                }));

        // keyboard
        addListener(new SimpleKeyboardListener("recording.combined.keyboard") {
            @Override
            public void keyPressed(KeyPressEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }

            @Override
            public void keyReleased(KeyReleaseEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }
        });


        // mouse
        addListener(new SimpleMouseListener("recording.combined.mouse") {
            @Override
            public void buttonPressed(MousePressEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }

            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }

            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }

            @Override
            public void mouseMoved(MouseMoveEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }
        });
    }

    @Override
    protected void onRecordingStarted() {
        eventLog.clear();
    }

    @Override
    protected void onRecordingStopped() {
        CombinedRecordingParams params = Dependency.getConfig().storage().combinedRecordingParams();
        String encodingType = params.getEncodingType();
        ActionEncoder actionEncoder = ActionEncoderFactory.get(encodingType);
        if (params.isIncludeKeyboard()) actionEncoder.addKeys();
        if (params.isIncludeMouseButtons()) actionEncoder.addMouseButtons();
        if (params.isIncludeMouseWheel()) actionEncoder.addMouseWheel();
        if (params.isAbsolute()) actionEncoder.absolute();
        if (params.isRelative()) actionEncoder.relative();
        if (params.isIncludeDelays()) actionEncoder.addDelays();
        if (params.isFixedRateOn()) actionEncoder.fixedRate(params.getFixedRate());
        if (params.isMinDistanceOn())
            actionEncoder.minDistance(params.getMinDistance());
        if (params.isStopDetectionOn())
            actionEncoder.detectStopPoints(params.getStopDetectionTime());

        String rawCode = actionEncoder.encode(EventsConverter.convertUselessEventToClickauto(eventLog.getEventLog()));

        putString(combinedCodeGenerator.forMethod("run", encodingType, rawCode));
    }
}
