package org.dikhim.jclicker.controllers.utils;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.ToggleButton;
import org.apache.commons.collections4.map.MultiValueMap;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.WindowManager;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.recordingparams.RecordingParams;
import org.dikhim.jclicker.controllers.utils.recording.*;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.jsengine.clickauto.generators.CombinedObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.SystemObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.objects.ScreenObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.UselessScreenObject;
import org.dikhim.jclicker.ui.CodeTextArea;
import org.dikhim.jclicker.util.ShapeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class EventsRecorder {
    private String prefix = "recorder";

    private KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
    private MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();

    private KeyboardObjectCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator();
    private MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
    private CombinedObjectCodeGenerator combinedObjectCodeGenerator = new CombinedObjectCodeGenerator(120);
    private SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator();

    private EventLogger eventLog = new EventLogger(10000);
    private RecordingParams recordingParams;
    private MainConfiguration mainConfiguration;

    private CodeTextArea outputTextArea;

    private BooleanProperty recording = new SimpleBooleanProperty(false);

    private BooleanProperty mouseRecording = new SimpleBooleanProperty(false);
    private BooleanProperty keyboardRecording = new SimpleBooleanProperty(false);

    private BooleanProperty controlKeyPressed = new SimpleBooleanProperty(false);

    private Consumer<BufferedImage> onSetOutputImage;

    private EventManager eventManager = Dependency.getEventManager();

    private MultiValueMap<Object, Runnable> toggleButtonMam = new MultiValueMap<>();

    private Map<String, Recorder> recoders = new HashMap<>();

    public EventsRecorder(MainConfiguration mainConfiguration) {
        this.mainConfiguration = mainConfiguration;
        recordingParams = mainConfiguration.getRecordingParams();
    }

    public void combined() {
      /*  String prefix = this.prefix + ".combined";
        String control = getCombinedControl();
        Combined combinedConfig = recordingParams.getCombined();
        if (combinedConfig.isKeysIncluded()) startKeyboardRecording();
        if (combinedConfig.isMouseButtonsIncluded()
                || combinedConfig.isMouseWheelIncluded()
                || combinedConfig.isAbsolute()
                || combinedConfig.isRelative()) startMouseRecording();
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
                    putString(combinedObjectCodeGenerator.getGeneratedCode());
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
        }));*/
    }


    // screen
    public void selectImage() {
      /*  startMouseRecording();
        final Point point0 = new Point();
        final Point point1 = new Point();
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.press", getMouseControl(), "PRESS", (e) -> {
            point0.x = mouseEventsManager.getX();
            point0.y = mouseEventsManager.getY();

        }));
        keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                prefix + ".control.release", getMouseControl(), "RELEASE", controlEvent -> {
            point1.x = mouseEventsManager.getX();
            point1.y = mouseEventsManager.getY();

            if (onSetOutputImage == null) return;
            Platform.runLater(() -> {
                final ScreenObject screenObject = new UselessScreenObject(Dependency.getRobot());

                Rectangle rectangle = ShapeUtil.createRectangle(point1, point0);
                rectangle.height++;
                rectangle.width++;
                BufferedImage bufferedImage = screenObject.getImage(rectangle);

                onSetOutputImage.accept(bufferedImage);
            });
        }));
        */
    }

    // miscellaneous

    public void filePath() {
        File file = WindowManager.getInstance().openFile();
        if (file != null) {
            putCode(file.getAbsolutePath());
        }
    }

    //////////
    public String getPrefix() {
        return prefix;
    }

    // private 
    public void putCode(String code) {
        if (outputTextArea == null) return;
        Platform.runLater(() -> outputTextArea.insertTextIntoCaretPosition(code));
    }

    public void puImage(BufferedImage bufferedImage) {
        onSetOutputImage.accept(bufferedImage);
    }

    private String getMouseControl() {
        return mainConfiguration.getHotKeys().getShortcut("mouseControl").getKeys().get();
    }

    private String getCombinedControl() {
        return mainConfiguration.getHotKeys().getShortcut("combinedControl").getKeys().get();
    }

    // setters
    public void setOutputTextArea(CodeTextArea outputTextArea) {
        this.outputTextArea = outputTextArea;
    }

    public void setRecording(boolean recording) {
        this.recording.set(recording);
    }

    public void stopRecording() {
        stopKeyboardRecording();
        stopMouseRecording();
    }


    public void startKeyboardRecording() {
        keyboardRecording.setValue(true);
    }

    public void stopKeyboardRecording() {
        keyboardRecording.setValue(false);
        removeKeyboardListeners();
    }


    private void startMouseRecording() {
        mouseRecording.setValue(true);
    }

    private void stopMouseRecording() {
        mouseRecording.setValue(false);
        removeMouseListeners();
    }

    private void setOutputImage(Point p1, Point p2) {
        if (onSetOutputImage == null) return;
        Platform.runLater(() -> {
            final ScreenObject screenObject = new UselessScreenObject(Dependency.getRobot());

            Rectangle rectangle = ShapeUtil.createRectangle(p1, p2);
            rectangle.height++;
            rectangle.width++;
            BufferedImage bufferedImage = screenObject.getImage(rectangle);

            onSetOutputImage.accept(bufferedImage);
        });
    }

    //
    private void removeKeyboardListeners() {
        keyEventsManager.removeListenersByPrefix(prefix);
    }

    void removeMouseListeners() {
        mouseEventsManager.removeListenersByPrefix(prefix);
    }


    public boolean isMouseRecording() {
        return mouseRecording.get();
    }

    public BooleanProperty mouseRecordingProperty() {
        return mouseRecording;
    }

    public boolean isKeyboardRecording() {
        return keyboardRecording.get();
    }

    public BooleanProperty keyboardRecordingProperty() {
        return keyboardRecording;
    }

    public void setOnSetOutputImage(Consumer<BufferedImage> onSetOutputImage) {
        this.onSetOutputImage = onSetOutputImage;
    }
    ////

    private List<Recorder> recorderList = new ArrayList<>();

    public void bindToggleButton(ToggleButton toggleButton, Recorder recorder) {
        if (recorder instanceof KeyRecorder) {
            Recorder finalRecorder = recorder;
            toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    startKeyboardRecording();
                    finalRecorder.start();
                } else {
                    stopKeyboardRecording();
                    finalRecorder.stop();
                }
            });
        } else if (recorder instanceof MouseRecorder) {
            Recorder finalRecorder1 = recorder;
            toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    startMouseRecording();
                    finalRecorder1.start();
                } else {
                    stopMouseRecording();
                    finalRecorder1.stop();
                }
            });
        } else if (recorder instanceof ScreenRecorder) {
            Recorder finalRecorder1 = recorder;
            toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    startMouseRecording();
                    finalRecorder1.start();
                } else {
                    stopMouseRecording();
                    finalRecorder1.stop();
                }
            });
        }


    }

}
