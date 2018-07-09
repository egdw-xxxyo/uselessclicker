package org.dikhim.jclicker.controllers.utils;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
import org.dikhim.jclicker.jsengine.objects.JsScreenObject;
import org.dikhim.jclicker.jsengine.objects.ScreenObject;
import org.dikhim.jclicker.jsengine.objects.generators.CombinedObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.SystemObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.robot.RobotStatic;
import org.dikhim.jclicker.util.ImageUtil;
import org.dikhim.jclicker.util.ShapeUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;
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
    private ImageView previewImage;
    private ImageView outputImage;
    private Pane previewPane;

    private BooleanProperty recording = new SimpleBooleanProperty(false);

    private BooleanProperty controlKeyPressed = new SimpleBooleanProperty(false);

    private IntegerProperty resolution = new SimpleIntegerProperty(33);

    public EventsRecorder(MainConfiguration mainConfiguration) {
        this.mainConfiguration = mainConfiguration;
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".clickAt";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".move";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".moveTo";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".pressAt";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".releaseAt";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".buttonAndWheelAt";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".buttonsAndWheelAtWithDelays";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".moveAndButtonAndWheel";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".click";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".wheelAt";
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
        showImageOnMouseMove();
        String prefix = this.prefix + ".combined";
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

    
    // screen
    public void selectImage() {
        showImageOnMouseMove();
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

            setOutputImage(point1, point0);
        }));
    }

    //////////
    private void showImageOnMouseMove() {
        startMouseRecording();

        final ScreenObject screenObject = new JsScreenObject(RobotStatic.get());
        ImageCapturer imageCapturer = new ImageCapturer();
        imageCapturer.setScreenObject(screenObject);
        imageCapturer.setOnImageLoaded(this::setPreviewImage);

        BiConsumer<Integer, Integer> onMove = (x, y) -> {
            if (!imageCapturer.isLocked()) {
                int rectSize = resolution.get();
                int x0 = x - rectSize / 2;
                int y0 = y - rectSize / 2;
                int x1 = x0 + rectSize;
                int y1 = y0 + rectSize;
                imageCapturer.captureImage(x0, y0, x1, y1);
            }
        };

        onMove.accept(mouseEventsManager.getX(), mouseEventsManager.getY());

        mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".show.on.move", new Consumer<MouseMoveEvent>() {
            @Override
            public void accept(MouseMoveEvent e) {
                onMove.accept(e.getX(), e.getY());
            }
        }));
    }

    public String getPrefix() {
        return prefix;
    }

    // private 
    private void putCode(String code) {
        if (outputTextArea == null) return;
        Platform.runLater(() -> outputTextArea.insertTextIntoCaretPosition(code));
    }

    private void setPreviewImage(BufferedImage bufferedImage) {
        if (previewImage == null) return;
        int w = (int) previewImage.getFitWidth();
        int h = (int) previewImage.getFitHeight();
        BufferedImage resizedImage = ImageUtil.resizeImage(bufferedImage, w, h);
        Image image = SwingFXUtils.toFXImage(resizedImage, null);
        previewImage.setImage(image);
    }

    private void setOutputImage(Point p1, Point p2) {
        if (outputImage == null) return;
        Platform.runLater(() -> {
            final ScreenObject screenObject = new JsScreenObject(RobotStatic.get());

            Rectangle rectangle = ShapeUtil.createRectangle(p1, p2);
            rectangle.height++;
            rectangle.width++;
            BufferedImage bufferedImage = screenObject.getImage(rectangle);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            outputImage.setImage(image);
        });
    }

    private String getMouseControl() {
        return mainConfiguration.getHotKeys().getShortcut("mouseControl").getKeys().get();
    }

    private String getCombinedControl() {
        return mainConfiguration.getHotKeys().getShortcut("combinedControl").getKeys().get();
    }

    // setters


    public void setPreviewPane(Pane previewPane) {
        this.previewPane = previewPane;
        ObservableList<Node> children = previewPane.getChildren();
        previewImage = (ImageView) children.stream().filter(c -> "previewImage".equals(c.getId())).findFirst().get();
        Pane previewButtonPane = (Pane) children.stream().filter(c -> "previewButtonPane".equals(c.getId())).findFirst().get();
        Button zoomIn = (Button) previewButtonPane.getChildren().stream().filter(c -> "zoom-in".equals(c.getId())).findFirst().get();
        zoomIn.setOnAction(this::zoomIn);
        Button zoomOut = (Button) previewButtonPane.getChildren().stream().filter(c -> "zoom-out".equals(c.getId())).findFirst().get();
        zoomOut.setOnAction(this::zoomOut);

        previewPane.visibleProperty().bindBidirectional(mouseRecording);
    }

    public void setOutputTextArea(CodeTextArea outputTextArea) {
        this.outputTextArea = outputTextArea;
    }

    public void setOutputImageView(ImageView outputImage) {
        this.outputImage = outputImage;
    }

    public void setPreviewImageView(ImageView previewImage) {
        this.previewImage = previewImage;
    }

    public int getResolution() {
        return resolution.get();
    }

    public IntegerProperty resolutionProperty() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution.set(resolution);
    }

    public void zoomIn(ActionEvent event) {
        int value = resolution.getValue();
        if (value > 9) {
            resolution.setValue(resolution.getValue() / 2 + 1);
        }
    }

    public void zoomOut(ActionEvent event) {
        int value = resolution.getValue();
        if (value < 129) {
            resolution.setValue((resolution.getValue() - 1) * 2);
        }
    }

    public void setRecording(boolean recording) {
        this.recording.set(recording);
    }

    public void stopRecording() {
        stopKeyboardRecording();
        stopMouseRecording();
    }

    private BooleanProperty keyboardRecording = new SimpleBooleanProperty(false);
    public void startKeyboardRecording() {
        keyboardRecording.setValue(true);

    }

    public void stopKeyboardRecording() {
        keyboardRecording.setValue(false);
        removeKeyboardListeners();
    }

    private BooleanProperty mouseRecording = new SimpleBooleanProperty(false);
    public void startMouseRecording() {
        mouseRecording.setValue(true);
    }

    public void stopMouseRecording() {
        mouseRecording.setValue(false);
        removeMouseListeners();
    }

    //
    private void removeKeyboardListeners() {
        keyEventsManager.removeListenersByPrefix(prefix);
    }

    void removeMouseListeners() {
        mouseEventsManager.removeListenersByPrefix(prefix);
    }
    
    

}
