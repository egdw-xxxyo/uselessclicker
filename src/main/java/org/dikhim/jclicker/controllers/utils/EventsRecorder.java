package org.dikhim.jclicker.controllers.utils;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.ImageView;
import org.dikhim.componentlibrary.components.CodeTextArea;
import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.events.MouseButtonEvent;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoder;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.recordingparams.Combined;
import org.dikhim.jclicker.configuration.recordingparams.RecordingParams;
import org.dikhim.jclicker.jsengine.objects.generators.CombinedObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.SystemObjectCodeGenerator;

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
    private MainConfiguration mainConfiguration;

    private CodeTextArea outputTextArea;
    private ImageView outputImage;

    private BooleanProperty recording = new SimpleBooleanProperty(false);
    private BooleanProperty controlKeyPressed = new SimpleBooleanProperty(false);

    public EventsRecorder(MainConfiguration mainConfiguration, CodeTextArea outputTextArea) {
        this.mainConfiguration = mainConfiguration;
        this.outputTextArea = outputTextArea;
        recordingParams = mainConfiguration.getRecordingParams();
    }

    // keyboard

    public void keyName() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".press", "", "PRESS", e -> {
            putCode(e.getKey() + " ");
        }));
    }

    public void keyPerform() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".perform", "", "", (e) -> {
            keyboardObjectCodeGenerator.perform(e.getKey(), e.getAction());
            putCode(keyboardObjectCodeGenerator.getGeneratedCode());
        }));
    }

    public void keyPerformWithDelays() {
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
            putCode(code);
        }));
    }

    // mouse basics

    public void insertMouseName() {
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", getMouseControl(), "PRESS", (controlEvent) -> {
            mouseEventsManager.addButtonListener(
                    new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                        putCode(e.getButton() + " ");
                    }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", getMouseControl(), "RELEASE", (controlEvent) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));

    }

    public void insertMouseClick() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + "control.press", getMouseControl(), "PRESS", controlEvent -> {
            eventLog.clear();
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                eventLog.add(e);
            }));
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                if (eventLog.isEmpty()) return;
                MouseButtonEvent lastMouseButtonEvent = eventLog.getLastMouseButtonEvent();

                if (lastMouseButtonEvent.getButton().equals(e.getButton()) &&
                        lastMouseButtonEvent.getX() == e.getX() &&
                        lastMouseButtonEvent.getY() == e.getY()) {
                    mouseObjectCodeGenerator.click(e.getButton());
                    putCode(mouseObjectCodeGenerator.getGeneratedCode());
                }
                eventLog.clear();
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + "control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void insertMouseClickAt() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + "control.press", getMouseControl(), "PRESS", controlEvent -> {
            eventLog.clear();
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                eventLog.add(e);
            }));
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                if (eventLog.isEmpty()) return;
                MouseButtonEvent lastMouseButtonEvent = eventLog.getLastMouseButtonEvent();
                if (lastMouseButtonEvent.getButton().equals(e.getButton()) &&
                        lastMouseButtonEvent.getX() == e.getX() &&
                        lastMouseButtonEvent.getY() == e.getY()) {
                    mouseObjectCodeGenerator.clickAt(e.getButton(), e.getX(), e.getY());
                    putCode(mouseObjectCodeGenerator.getGeneratedCode());
                }
                eventLog.clear();
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + "control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void insertMouseMove() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            eventLog.clear();
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                eventLog.add(e);
            }));
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                if (eventLog.isEmpty()) return;
                int dx = eventLog.getMouseEventDx();
                int dy = eventLog.getMouseEventDy();
                mouseObjectCodeGenerator.move(dx, dy);
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
                eventLog.clear();
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void insertMouseMoveTo() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {

                mouseObjectCodeGenerator.moveTo(e.getX(), e.getY());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void insertMousePress() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                mouseObjectCodeGenerator.press(e.getButton());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void insertMousePressAt() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                mouseObjectCodeGenerator.pressAt(e.getButton(), e.getX(), e.getY());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void insertMouseRelease() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                mouseObjectCodeGenerator.release(e.getButton());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void insertMouseReleaseAt() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                mouseObjectCodeGenerator.releaseAt(e.getButton(), e.getX(), e.getY());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    // mouse

    public void mouseButtonAndWheelAt() {
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", getMouseControl(), "PRESS", (controlEvent) -> {
            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", (e) -> {
                mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", (e) -> {
                mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", getMouseControl(), "RELEASE", (e) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void mouseButtonAndWheelAtWithDelays() {
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.press", getMouseControl(), "PRESS", (controlEvent) -> {
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
                putCode(code);
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
                putCode(code);
            }));
        }));

        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.release", getMouseControl(), "RELEASE", (e) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void mouseMoveAndButtonAndWheel() {
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", getMouseControl(), "PRESS", (controlEvent) -> {
            eventLog.clear();
            eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", e -> {
                if (eventLog.isEmpty()) return;
                eventLog.add(e);

                int dx = eventLog.getMouseEventDx();
                int dy = eventLog.getMouseEventDy();

                mouseObjectCodeGenerator.moveAndButton(e.getButton(), e.getAction(), dx, dy);
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));

            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                if (eventLog.isEmpty()) return;
                eventLog.add(e);

                int dx = eventLog.getMouseEventDx();
                int dy = eventLog.getMouseEventDy();

                mouseObjectCodeGenerator.moveAndWheel(e.getDirection(), e.getAmount(), dx, dy);
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));

        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", getMouseControl(), "RELEASE", (controlEvent) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void click() {
        final int[] a = new int[1];
        keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", getMouseControl(), "PRESS", (controlEvent) -> {
            eventLog.clear();
            // eventLog must be empty except when PRESS event occurs

            mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                if (eventLog.getMouseButtonLogSize() > 0) {
                    // if not empty put PRESS code for last event
                    MouseButtonEvent lastE = eventLog.getLastMouseButtonEvent();
                    mouseObjectCodeGenerator.buttonAt(lastE.getButton(), lastE.getAction(), lastE.getX(), lastE.getY());
                    putCode(mouseObjectCodeGenerator.getGeneratedCode());
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
                putCode(code);
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
                prefix + ".control.release", getMouseControl(), "RELEASE", (e) -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void wheel() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                mouseObjectCodeGenerator.wheel(e.getDirection(), e.getAmount());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void wheelAt() {
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", controlEvent -> {
            mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                putCode(mouseObjectCodeGenerator.getGeneratedCode());
            }));
        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            mouseEventsManager.removeListenersByPrefix(prefix);
        }));
    }

    public void combined() {
        String control = getCombinedControl();
        Combined combinedConfig = recordingParams.getCombined();
        final boolean[] recording = new boolean[1];
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".start", control, "RELEASE", controlEvent -> {

            if (recording[0]) {
                recording[0] = false;
                return;
            }
            // start recording on release
            recording[0] = true;
            eventLog.clear();
            eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + ".keys", "", "", e -> {
                if (e.getKey().equals(control) && e.getAction().equals("PRESS")) {
                    // stop on press
                    keyEventsManager.removeListenersByPrefix(prefix + ".keys");
                    mouseEventsManager.removeListenersByPrefix(prefix);

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


                    String rawCode = actionEncoder.encode(eventLog.getEventLog());
                    combinedObjectCodeGenerator.run(encodingType, rawCode);
                    putCode(combinedObjectCodeGenerator.getGeneratedCode());
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
    
    public void selectImage(){
        System.out.println("select image");
    }

    public String getPrefix() {
        return prefix;
    }

    // private 
    private void putCode(Consumer<String> consumer, String code) {
        Platform.runLater(() -> consumer.accept(code));
    }

    private void putCode(String code) {
        Platform.runLater(() -> outputTextArea.insertTextIntoCaretPosition(code));
    }

    private void setImage() {
        if (outputImage != null) {
            System.out.println("set image");
        }
    }
    
    private String getMouseControl() {
        return mainConfiguration.getHotKeys().getShortcut("mouseControl").getKeys().get();
    }

    private String getCombinedControl() {
        return mainConfiguration.getHotKeys().getShortcut("combinedControl").getKeys().get();
    }
    
    // setters

    public void setOutputImage(ImageView outputImage) {
        this.outputImage = outputImage;
    }
}
