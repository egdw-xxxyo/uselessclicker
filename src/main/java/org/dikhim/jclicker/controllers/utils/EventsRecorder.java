package org.dikhim.jclicker.controllers.utils;

import javafx.application.Platform;
import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.events.MouseButtonEvent;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.actions.utils.MouseMoveEventUtil;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoder;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.actions.utils.encoders.UnicodeActionEncoder;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.recordingparams.Combined;
import org.dikhim.jclicker.configuration.recordingparams.RecordingParams;
import org.dikhim.jclicker.jsengine.objects.generators.CombinedObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.SystemObjectCodeGenerator;

import java.util.List;
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

    // keyboard

    public void keyName(Consumer<String> onGenerateCode) {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".press", "", "PRESS", e -> {
            putCode(onGenerateCode, e.getKey() + " ");
        }));
    }

    public void keyPerform(Consumer<String> onGenerateCode) {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".perform", "", "", (e) -> {
            keyboardObjectCodeGenerator.perform(e.getKey(), e.getAction());
            putCode(onGenerateCode, keyboardObjectCodeGenerator.getGeneratedCode());
        }));
    }

    public void keyPerformWithDelays(Consumer<String> onGenerateCode) {
        eventLog.clear();
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + ".perform", "", "", (e) -> {
            eventLog.add(e);

            String code = "";
            if (eventLog.size() > 1) {
                int delay = eventLog.getDelay();
                systemObjectCodeGenerator.sleep(delay);
                code += systemObjectCodeGenerator.getGeneratedCode();
            }

            keyboardObjectCodeGenerator.perform(e.getKey(), e.getAction());
            code += keyboardObjectCodeGenerator.getGeneratedCode();
            putCode(onGenerateCode, code);
        }));
    }

    // mouse

    public void mouseButtonAndWheelAt(Consumer<String> onGenerateCode) {
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", (e) -> {
                mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                putCode(onGenerateCode, mouseObjectCodeGenerator.getGeneratedCode());
            }));
            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", (e) -> {
                mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                putCode(onGenerateCode, mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", "CONTROL", "RELEASE", (e) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void mouseButtonAndWheelAtWithDelays(Consumer<String> onGenerateCode) {
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
            eventLog.clear();
            eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", (e) -> {
                if (eventLog.isEmpty()) return;
                eventLog.add(e);
                String code = "";

                int delay = eventLog.getDelay();
                systemObjectCodeGenerator.sleep(delay);
                code += systemObjectCodeGenerator.getGeneratedCode();

                mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putCode(onGenerateCode, code);
            }));
            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", (e) -> {
                if (eventLog.isEmpty()) return;
                eventLog.add(e);
                String code = "";

                int delay = eventLog.getDelay();
                systemObjectCodeGenerator.sleep(delay);
                code += systemObjectCodeGenerator.getGeneratedCode();

                mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                putCode(onGenerateCode, code);
            }));
        }));

        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.release", "CONTROL", "RELEASE", (e) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void mouseMoveAndButtonAndWheel(Consumer<String> onGenerateCode) {
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
            eventLog.clear();
            eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", e -> {
                if (eventLog.isEmpty()) return;
                eventLog.add(e);

                int dx = eventLog.getMouseEventDx();
                int dy = eventLog.getMouseEventDy();

                mouseObjectCodeGenerator.moveAndButton(e.getButton(), e.getAction(), dx, dy);
                putCode(onGenerateCode, mouseObjectCodeGenerator.getGeneratedCode());
            }));

            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                if (eventLog.isEmpty()) return;
                eventLog.add(e);

                int dx = eventLog.getMouseEventDx();
                int dy = eventLog.getMouseEventDy();

                mouseObjectCodeGenerator.moveAndWheel(e.getDirection(), e.getAmount(), dx, dy);
                putCode(onGenerateCode, mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));

        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", "CONTROL", "RELEASE", (controlEvent) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }
    // TODO bug in click recording

    public void click(Consumer<String> onGenerateCode) {
        final int[] a = new int[1];
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
            eventLog.clear();
            // eventLog must be empty except when PRESS event occurs

            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                if (eventLog.getMouseButtonLogSize() > 0) {
                    // if not empty put PRESS code for last event
                    MouseButtonEvent lastE = eventLog.getLastMouseButtonEvent();
                    mouseObjectCodeGenerator.buttonAt(lastE.getButton(), lastE.getAction(), lastE.getX(), lastE.getY());
                    putCode(onGenerateCode, mouseObjectCodeGenerator.getGeneratedCode());
                }
                eventLog.clear();
                eventLog.add(e);
            }));

            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                String code = "";
                if (eventLog.getMouseButtonLogSize() > 0) {
                    MouseButtonEvent lastE = eventLog.getLastMouseButtonEvent();
                    if (lastE.getButton().equals(e.getButton()) &&
                            lastE.getAction().equals("PRESS") &&
                            lastE.getX() == e.getX() &&
                            lastE.getY() == e.getY()) {
                        // if last event equals to current and it is PRESS event then CLICK
                        mouseObjectCodeGenerator.buttonAt(e.getButton(), "CLICK", e.getX(), e.getY());
                        code += mouseObjectCodeGenerator.getGeneratedCode();
                    } else {
                        // if not CLICK then put two RELEASE event for both events last and current
                        mouseObjectCodeGenerator.buttonAt(lastE.getButton(), lastE.getAction(), lastE.getX(), lastE.getY());
                        code += mouseObjectCodeGenerator.getGeneratedCode();
                        mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                        code += mouseObjectCodeGenerator.getGeneratedCode();
                    }
                } else {
                    // if eventLog is empty put RELEASE code for current event
                    mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                    code += mouseObjectCodeGenerator.getGeneratedCode();
                }
                putCode(onGenerateCode, code);
                eventLog.clear();
            }));

            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                String code = "";
                if (eventLog.getMouseButtonLogSize() > 0) {
                    // if eventLog is not empty put PRESS code for last event
                    MouseButtonEvent lastE = eventLog.getLastMouseButtonEvent();
                    mouseObjectCodeGenerator.buttonAt(lastE.getButton(), lastE.getAction(), lastE.getX(), lastE.getY());
                    code += mouseObjectCodeGenerator.getGeneratedCode();
                }
                // finally put WHEEL code
                mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                code += mouseObjectCodeGenerator.getGeneratedCode();
                eventLog.clear();
            }));
        }));

        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                prefix + ".control.release", "CONTROL", "RELEASE", (e) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void wheel(Consumer<String> onGenerateCode) {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                mouseObjectCodeGenerator.wheel(e.getDirection(), e.getAmount());
                putCode(onGenerateCode, mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void wheelAt(Consumer<String> onGenerateCode) {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                putCode(onGenerateCode, mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
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

                    ActionEncoder unicodeActionEncoder = ActionEncoderFactory.get("unicode");
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
                    putCode(onGenerateCode, combinedObjectCodeGenerator.getGeneratedCode());
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

    private void putCode(Consumer<String> consumer, String code) {
        Platform.runLater(() -> consumer.accept(code));
    }
}
