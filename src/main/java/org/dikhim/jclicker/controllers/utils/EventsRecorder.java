package org.dikhim.jclicker.controllers.utils;

import javafx.application.Platform;
import org.dikhim.jclicker.actions.MouseButtonHandler;
import org.dikhim.jclicker.actions.MouseMoveHandler;
import org.dikhim.jclicker.actions.MouseWheelHandler;
import org.dikhim.jclicker.actions.ShortcutIncludesListener;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.actions.utils.encoders.UnicodeActionEncoder;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.recordingparams.Combined;
import org.dikhim.jclicker.configuration.recordingparams.RecordingParams;
import org.dikhim.jclicker.jsengine.objects.generators.CombinedObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.SystemObjectCodeGenerator;

import java.util.concurrent.Future;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class EventsRecorder {
    private String prefix = "recorder";

    private KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
    private MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();

    private KeyboardObjectCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator();
    private MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
    private CombinedObjectCodeGenerator combinedObjectCodeGenerator = new CombinedObjectCodeGenerator();
    private SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator();

    private EventLogger eventLog = new EventLogger(10000);

    private RecordingParams recordingParams;

    public EventsRecorder(RecordingParams recordingParams) {
        this.recordingParams = recordingParams;
    }


    public void combined(Consumer<String> onGenerateCode) {
        Combined combinedConfig = recordingParams.getCombined();
        final boolean[] recording = new boolean[1];
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".start", combinedConfig.getControlKey(), "RELEASE", controlEvent -> {

            if (recording[0]) {
                recording[0] = false;
                return;
            }
            // start recording on release
            recording[0] = true;
            eventLog.clear();
            eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + ".keys", "", "", e -> {
                if (e.getKey().equals(combinedConfig.getControlKey()) && e.getAction().equals("PRESS")) {
                    // stop on press
                    keyEventsManager.removeListenersByPrefix(prefix + ".keys");
                    mouseEventsManager.removeListenersByPrefix(prefix);

                    UnicodeActionEncoder unicodeActionEncoder = new UnicodeActionEncoder();
                    if (combinedConfig.isKeysIncluded()) unicodeActionEncoder.addKeys();
                    if (combinedConfig.isMouseButtonsIncluded()) unicodeActionEncoder.addMouseButtons();
                    if (combinedConfig.isMouseWheelIncluded()) unicodeActionEncoder.addMouseWheel();
                    if (combinedConfig.isAbsolute()) unicodeActionEncoder.absolute();
                    if (combinedConfig.isRelative()) unicodeActionEncoder.relative();
                    if (combinedConfig.isDelaysIncluded()) unicodeActionEncoder.addDelays();
                    if (combinedConfig.isFixedRateOn()) unicodeActionEncoder.fixedRate(combinedConfig.getFixedRate());
                    if (combinedConfig.isMinDistanceOn())
                        unicodeActionEncoder.minDistance(combinedConfig.getMinDistance());
                    if (combinedConfig.isStopDetectionOn())
                        unicodeActionEncoder.detectStopPoints(combinedConfig.getStopDetectionTime());


                    String rawCode = unicodeActionEncoder.encode(eventLog.getEventLog());
                    combinedObjectCodeGenerator.run(rawCode);
                    Platform.runLater(() -> onGenerateCode.accept(combinedObjectCodeGenerator.getGeneratedCode()));
                } else {
                    eventLog.add(e);
                }
            }));

            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", e -> {
                eventLog.add(e);
            }));

            mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", e -> {
                eventLog.add(e);
            }));

            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                eventLog.add(e);
            }));
        }));
    }

    public String getPrefix() {
        return prefix;
    }
}
