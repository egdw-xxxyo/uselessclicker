package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.clickauto.jsengine.utils.encoders.ActionEncoder;
import org.dikhim.clickauto.jsengine.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.configuration.recordingparams.Combined;
import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.listener.KeyListener;
import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;
import org.dikhim.jclicker.eventmanager.listener.MouseListener;
import org.dikhim.jclicker.jsengine.clickauto.EventsConverter;
import org.dikhim.jclicker.jsengine.clickauto.generators.CombinedCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.CombinedObjectOldCodeGenerator;

import java.util.function.Consumer;

/**
 * combined.run("......
 */
public class CombinedRecorder extends StringRecorder implements MouseRecorder, KeyRecorder,LupeRequired {
    public CombinedRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private CombinedCodeGenerator combinedCodeGenerator;
    private Combined combinedConfig;
    private String control;
    private EventLogger eventLog;
    private boolean ignoreNextKeyRelease = false;

    @Override
    public void onStart() {
        combinedCodeGenerator = new CombinedCodeGenerator();
        combinedConfig = Dependency.getConfig().getRecordingParams().getCombined();
        control = combinedConfig.getControlKey();
        eventLog = new EventLogger();
        // control button listener. Start on release and stop after press
        addListener("recording.combined.control", new KeyPressReleaseListener(control,
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
        addListener("recording.combined.keyboard", new KeyListener() {
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
        addListener("recording.combined.mouse", new MouseListener() {
            @Override
            public void buttonReleased(MouseReleaseEvent event) {
                if (!isRecording()) return;
                eventLog.add(event);
            }

            @Override
            public void buttonPressed(MousePressEvent event) {
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
        String encodingType = combinedConfig.getEncodingType();
        ActionEncoder actionEncoder = ActionEncoderFactory.get(encodingType);
        if (combinedConfig.isKeysIncluded()) actionEncoder.addKeys();
        if (combinedConfig.isMouseButtonsIncluded()) actionEncoder.addMouseButtons();
        if (combinedConfig.isMouseWheelIncluded()) actionEncoder.addMouseWheel();
        if (combinedConfig.isAbsolute()) actionEncoder.absolute();
        if (combinedConfig.isRelative()) actionEncoder.relative();
        if (combinedConfig.isDelaysIncluded()) actionEncoder.addDelays();
        if (combinedConfig.isFixedRateOn()) actionEncoder.fixedRate(combinedConfig.getFixedRate());
        if (combinedConfig.isMinDistanceOn())
            actionEncoder.minDistance(combinedConfig.getMinDistance());
        if (combinedConfig.isStopDetectionOn())
            actionEncoder.detectStopPoints(combinedConfig.getStopDetectionTime());

        String rawCode = actionEncoder.encode(EventsConverter.convertUselessEventToClickauto(eventLog.getEventLog()));

        putString(combinedCodeGenerator.forMethod("run", encodingType, rawCode));
    }
}
