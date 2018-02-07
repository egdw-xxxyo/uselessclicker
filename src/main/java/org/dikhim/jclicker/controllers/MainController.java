package org.dikhim.jclicker.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.apache.commons.io.IOUtils;
import org.dikhim.jclicker.ClickerMain;
import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.events.MouseButtonEvent;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.actions.utils.MouseMoveEventUtil;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.recordingparams.Combined;
import org.dikhim.jclicker.controllers.utils.EventsRecorder;
import org.dikhim.jclicker.jsengine.objects.generators.*;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.util.SourcePropertyFile;
import org.dikhim.jclicker.util.output.Out;
import sample.CodeTextArea;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

@SuppressWarnings({"unused", "Duplicates", "CodeBlock2Expr", "StringBufferReplaceableByString", "StringConcatenationInLoop"})
public class MainController {

    private ClickerMain application = ClickerMain.getApplication();

    private MainApplication mainApplication = ClickerMain.getApplication().getMainApplication();
    private Preferences preferences = Preferences.userRoot().node(getClass().getName());
    private EventLogger eventLog = new EventLogger(10000);

    private MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
    private KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();

    private int lineSize = 60;
    private KeyboardObjectCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator(lineSize);
    private MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator(lineSize);
    private SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator(lineSize);
    private ClipboardObjectCodeGenerator clipboardObjectCodeGenerator = new ClipboardObjectCodeGenerator(lineSize);
    private CombinedObjectCodeGenerator combinedObjectCodeGenerator = new CombinedObjectCodeGenerator(lineSize);

    private EventsRecorder eventsRecorder = new EventsRecorder(mainApplication.getConfig().getRecordingParams());

    @FXML
    private void initialize() {
        // init text areas
        codeTextArea.textProperty().bindBidirectional(mainApplication.getScript().codeProperty());
        outTextArea.textProperty().bindBidirectional(Out.outProperty());
        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);

        btnScriptStatus.textProperty().bind(mainApplication.statusProperty());
        btnScriptStatus.selectedProperty().bindBidirectional(mainApplication.getJse().runningProperty());

        // init toggles and template buttons
        SourcePropertyFile propertyFile = new SourcePropertyFile();

        InputStream txtReader = getClass().getResourceAsStream("/strings/codesamples_ru.txt");
        try {
            propertyFile.setSource(IOUtils.toString(txtReader, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initToggles(propertyFile);
        initTemplateButtons(propertyFile);

        // Init script
        setToggleStatus(null);

        KeyEventsManager keyListener = KeyEventsManager.getInstance();
        keyListener.addKeyboardListener(
                new ShortcutEqualsListener("stopScript", "CONTROL ALT S", "PRESS", (e) -> {
                    Platform.runLater(this::stopScript);
                }));

        bindConfig();

    }


    private void bindConfig() {
        StringConverter<Number> stringConverter = new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                try {
                    return String.valueOf(object);
                } catch (Exception e) {
                    System.out.println(object);
                    return "";
                }
            }


            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (Exception e) {
                    System.out.println(string);
                    return 0;
                }
            }
        };

        MainConfiguration config = mainApplication.getConfig();
        Combined combined = config.getRecordingParams().getCombined();
        Bindings.bindBidirectional(txtCombinedControl.textProperty(), combined.getControlKeyValue().valueProperty());
        Bindings.bindBidirectional(txtCombinedFixRate.textProperty(), combined.getFixedRateValue().valueProperty(), stringConverter);
        Bindings.bindBidirectional(txtCombinedMinDistance.textProperty(), combined.getMinDistanceValue().valueProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCombinedDetectStopPoints.textProperty(), combined.getStopDetectionTimeValue().valueProperty(), new NumberStringConverter());

        Bindings.bindBidirectional(btnCombinedDelays.selectedProperty(), combined.getIncludeDelaysValue().valueProperty());
        Bindings.bindBidirectional(btnCombinedKeys.selectedProperty(), combined.getIncludeKeyboardValue().valueProperty());
        Bindings.bindBidirectional(btnCombinedMouseButtons.selectedProperty(), combined.getIncludeMouseButtonsValue().valueProperty());
        Bindings.bindBidirectional(btnCombinedMouseWheel.selectedProperty(), combined.getIncludeMouseWheelValue().valueProperty());
        Bindings.bindBidirectional(btnCombinedAbsolutePath.selectedProperty(), combined.getAbsoluteValue().valueProperty());
        Bindings.bindBidirectional(btnCombinedRelativePath.selectedProperty(), combined.getRelative().valueProperty());

        Bindings.bindBidirectional(btnCombinedFixRate.selectedProperty(), combined.getFixedRateOnValue().valueProperty());
        Bindings.bindBidirectional(btnCombinedMinDistance.selectedProperty(), combined.getMinDistanceOnValue().valueProperty());
        Bindings.bindBidirectional(btnCombinedDetectStopPoints.selectedProperty(), combined.getStopDetectionOnValue().valueProperty());
    }

    @FXML
    private Button btnNewFile;

    @FXML
    private Button btnRunScript;

    @FXML
    private Button btnStopScript;

    @FXML
    private Button btnOpenFile;

    @FXML
    private Button btnSaveFile;

    @FXML
    private Button btnShowSeverWindow;

    @FXML
    private Button btnShowConfigWindow;

    @FXML
    private CodeTextArea codeTextArea;

    @FXML
    private TextArea areaCodeSample;
    private StringProperty codeSampleProperty = new SimpleStringProperty("");

    @FXML
    private TextArea outTextArea;
    private StringProperty outTextProperty = new SimpleStringProperty(" ");

    @FXML
    private TextField txtAbsolutePathRate;

    @FXML
    private TextField txtRelativePathRate;

    @FXML
    public void stopScript() {
        mainApplication.stopScript();
    }

    @FXML
    public void runScript() {
        Out.clear();
        select(null);
        mainApplication.runScript();
    }

    @FXML
    public void newFile() {
        mainApplication.newFile();
    }

    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All types", "*.*"),
                new FileChooser.ExtensionFilter("JavaScript", "*.js"));

        String pathFolder = preferences.get("last-opened-folder", "");
        if (!pathFolder.isEmpty()) {
            fileChooser.setInitialDirectory(new File(pathFolder));
        }
        File file = fileChooser.showOpenDialog(application.getPrimaryStage());
        if (file != null) {
            mainApplication.openFile(file);
            preferences.put("last-opened-folder", file.getParentFile().getAbsolutePath());
        }
    }

    @FXML
    public void saveFile() {
        Script script = mainApplication.getScript();
        if (script.isOpened()) {
            mainApplication.saveFile();
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.setInitialFileName("newFile.js");

            String pathFolder = preferences.get("last-saved-folder", "");
            if (!pathFolder.isEmpty())
                fileChooser.setInitialDirectory(new File(pathFolder));

            File file = fileChooser.showSaveDialog(application.getPrimaryStage());
            if (file != null) {
                mainApplication.saveFileAs(file);
                preferences.put("last-saved-folder", file.getParentFile().getAbsolutePath());
            }
        }
    }

    /**
     * Save script in new file
     */
    @FXML
    public void saveFileAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName("newFile.js");

        String pathFolder = preferences.get("last-saved-folder", "");
        if (!pathFolder.isEmpty())
            fileChooser.setInitialDirectory(new File(pathFolder));

        File file = fileChooser.showSaveDialog(application.getPrimaryStage());
        if (file != null) {
            mainApplication.saveFileAs(file);
            preferences.put("last-saved-folder", file.getParentFile().getAbsolutePath());
        }
    }

    @FXML
    public void showServerWindow() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();

            root = loader.load(getClass().getResource("/ui/serverScene/ServerScene.fxml").openStream());
            Stage stage = new Stage();
            stage.setTitle("Server");
            stage.setScene(new Scene(root, 600, 200));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void showConfigWindow() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();

            root = loader.load(getClass().getResource("/ui/serverScene/ServerScene.fxml").openStream());
            Stage stage = new Stage();
            stage.setTitle("Server");
            stage.setScene(new Scene(root, 600, 200));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    ////// Toggles
    // Keyboard
    @FXML
    private ToggleButton btnInsertKeyName;

    @FXML
    private ToggleButton btnInsertKeyCode;

    @FXML
    private ToggleButton btnInsertKeyCodeWithDelay;

    // Mouse basics
    @FXML
    private ToggleButton btnInsertMouseName;

    @FXML
    private ToggleButton btnInsertMouseClick;

    @FXML
    private ToggleButton btnInsertMouseClickAt;

    @FXML
    private ToggleButton btnInsertMousePress;

    @FXML
    private ToggleButton btnInsertMousePressAt;

    @FXML
    private ToggleButton btnInsertMouseRelease;

    @FXML
    private ToggleButton btnInsertMouseReleaseAt;

    @FXML
    private ToggleButton btnInsertMouseMove;

    @FXML
    private ToggleButton btnInsertMouseMoveTo;

    @FXML
    private ToggleButton btnInsertMouseWheel;

    // mouse code
    @FXML
    private ToggleButton btnInsertMouseCode;

    @FXML
    private ToggleButton btnInsertMouseCodeWithDelay;

    @FXML
    private ToggleButton btnInsertMouseRelativeCode;
    // click
    @FXML
    private ToggleButton btnInsertMouseCodeClick;

    // Combined
    @FXML
    private ToggleButton btnInsertCombinedLog;

    @FXML
    TextField txtCombinedControl;

    @FXML
    TextField txtCombinedDetectStopPoints;

    @FXML
    TextField txtCombinedFixRate;

    @FXML
    TextField txtCombinedMinDistance;

    // Simple toggles
    @FXML
    ToggleButton btnCombinedKeys;

    @FXML
    ToggleButton btnCombinedMouseButtons;

    @FXML
    ToggleButton btnCombinedMouseWheel;

    @FXML
    ToggleButton btnCombinedAbsolutePath;

    @FXML
    ToggleButton btnCombinedRelativePath;

    @FXML
    ToggleButton btnCombinedDetectStopPoints;

    @FXML
    ToggleButton btnCombinedDelays;

    @FXML
    ToggleButton btnCombinedFixRate;

    @FXML
    ToggleButton btnCombinedMinDistance;


    private List<ToggleButton> listOfInsertCodeToggles = new ArrayList<>();

    private List<ToggleButton> simpleToggles = new ArrayList<>();

    /**
     * Adds all toggles to listOfInsertCodeToggles and sets hints to user data from property file
     */
    private void initToggles(SourcePropertyFile properties) {
        // keyboard
        listOfInsertCodeToggles.add(btnInsertKeyName);
        listOfInsertCodeToggles.add(btnInsertKeyCode);
        listOfInsertCodeToggles.add(btnInsertKeyCodeWithDelay);

        // mouse basics
        listOfInsertCodeToggles.add(btnInsertMouseClick);
        listOfInsertCodeToggles.add(btnInsertMouseClickAt);
        listOfInsertCodeToggles.add(btnInsertMouseName);
        listOfInsertCodeToggles.add(btnInsertMouseMove);
        listOfInsertCodeToggles.add(btnInsertMouseMoveTo);
        listOfInsertCodeToggles.add(btnInsertMousePress);
        listOfInsertCodeToggles.add(btnInsertMousePressAt);
        listOfInsertCodeToggles.add(btnInsertMouseRelease);
        listOfInsertCodeToggles.add(btnInsertMouseReleaseAt);
        listOfInsertCodeToggles.add(btnInsertMouseWheel);

        // mouse press/release
        listOfInsertCodeToggles.add(btnInsertMouseCode);
        listOfInsertCodeToggles.add(btnInsertMouseCodeWithDelay);
        listOfInsertCodeToggles.add(btnInsertMouseRelativeCode);

        //mouse click
        listOfInsertCodeToggles.add(btnInsertMouseCodeClick);

         // combined
        listOfInsertCodeToggles.add(btnInsertCombinedLog);

        simpleToggles.add(btnCombinedAbsolutePath);
        simpleToggles.add(btnCombinedDelays);
        simpleToggles.add(btnCombinedDetectStopPoints);
        simpleToggles.add(btnCombinedFixRate);
        simpleToggles.add(btnCombinedKeys);
        simpleToggles.add(btnCombinedMinDistance);
        simpleToggles.add(btnCombinedMouseButtons);
        simpleToggles.add(btnCombinedMouseWheel);
        simpleToggles.add(btnCombinedRelativePath);
        // set user data 'String' hint
        List<ToggleButton> listOfToggles = new ArrayList<>();
        listOfToggles.addAll(simpleToggles);
        listOfToggles.addAll(listOfInsertCodeToggles);
        for (ToggleButton b : listOfToggles) {
            b.setUserData(new String[]{properties.get(b.getId()), ""});
            b.setOnMouseEntered(this::showCodeSample);
            b.setOnMouseExited(this::hideCodeSample);
        }
    }

    private String getToggleButtonPath(Object button) {
        String out = "";
        Node n = (Node) button;
        if (button instanceof Button) {
            out = ((Button) button).getText();

        } else if (button instanceof ToggleButton) {
            out = ((ToggleButton) button).getText();
        }

        do {
            if (n instanceof TitledPane) {
                out = ((TitledPane) n).getText() + "> " + out;
            }
            n = n.getParent();
        } while ((!(n instanceof AnchorPane)) && (n != null));
        return out;
    }


    /**
     * Deselect all toggles except the parameter
     *
     * @param toggle ToggleButton
     */
    private void select(ToggleButton toggle) {
        for (ToggleButton t : listOfInsertCodeToggles) {
            if (t.isSelected()) {
                if (t != toggle)
                    t.fire();
            }
        }
        setToggleStatus(toggle);
    }

    /**
     * When toggle has been deselected
     *
     * @param toggle ToggleButton
     */
    private void deselect(ToggleButton toggle) {
        setToggleStatus(toggle);
    }

    private void setToggleStatus(ToggleButton toggle) {
        if (toggle == null) {
            if (btnTogglesStatus.getUserData() == null) {
                btnTogglesStatus.setText("Never used");
                btnTogglesStatus.setUserData(null);
                return;
            }
            return;
        }
        btnTogglesStatus.setSelected(toggle.isSelected());
        String title = "";
        if (toggle.isSelected()) {
            title += "Active:    ";
        } else {
            title += "Last used: ";
        }
        title += getToggleButtonPath(toggle);
        btnTogglesStatus.setText(title);
        btnTogglesStatus.setUserData(toggle);
    }

    private void onToggleButtonPerformed(ActionEvent event, Consumer<String> consumer) {
        ToggleButton toggleButton = (ToggleButton) event.getSource();
        if (toggleButton == null) return;

        String prefix = eventsRecorder.getPrefix();
        if (toggleButton.isSelected()) {
            select(toggleButton);
            // if toggle has been selected
            codeTextArea.setActive(false);
            consumer.accept(prefix);
        } else {
            deselect(toggleButton);
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            codeTextArea.setActive(true);
        }
    }


    // Keyboard

    /**
     * 'KEY1 KEY2 '
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertKeyName(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.keyName((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });

        });
    }

    /**
     * key.perform('KEY1','PRESS');<p>
     * key.perform('KEY1','RELEASE');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertKeyCode(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.keyPerform((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    /**
     * key.perform('KEY1','PRESS');<br>
     * system.sleep(100);<br>
     * key.perform('KEY1','RELEASE');<br>
     * system.sleep(100);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertKeyCodeWithDelay(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.keyPerformWithDelays((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    // Mouse buttons

    /**
     * mouse.buttonAt('LEFT','PRESS',1004,353);<br>
     * mouse.buttonAt('LEFT','RELEASE',1004,353);<br>
     * mouse.wheelAt('DOWN',3,1111,344);<br>
     * mouse.wheelAt('DOWN',3,1211,348);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseCode(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.mouseButtonAndWheelAt((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    /**
     * system.sleep(232);<br>
     * mouse.buttonAt('LEFT','PRESS',999,330);<br>
     * system.sleep(96);<br>
     * mouse.buttonAt('LEFT','RELEASE',999,330);<br>
     * system.sleep(592);<br>
     * mouse.wheelAt('DOWN',3,1191,353);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseCodeWithDelay(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.mouseButtonAndWheelAtWithDelays((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }


    /**
     * mouse.moveAndButton('LEFT',PRESS,-2,0);<br>
     * mouse.moveAndButton('LEFT',RELEASE,94,-27);<br>
     * mouse.moveAndWheel('DOWN',3,0,0);<br>
     * mouse.moveAndWheel('DOWN',3,0,0);<br>
     * mouse.moveAndWheel('DOWN',3,-13,22);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseRelativeCode(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.mouseMoveAndButtonAndWheel((code)->{
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    // Clicks

    /**
     * mouse.pressAt('LEFT',914,426);<br>
     * mouse.releaseAt('LEFT',915,438);<br>
     * mouse.clickAt('LEFT',918,502);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseCodeClick(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.click((code)->{
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    // mouse basics

    /**
     * 'LEFT RIGHT MIDDLE '
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseName(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
                mouseEventsManager.addButtonListener(
                        new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                            putTextIntoCaretPosition(codeTextArea, e.getButton() + " ");
                        }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.click('LEFT');<br>
     * mouse.click('RIGHT');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseClick(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.press", "CONTROL", "PRESS", controlEvent -> {
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
                        putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                    }
                    eventLog.clear();
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.clickAt('LEFT',685,375);<br>
     * mouse.clickAt('RIGHT',951,295);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseClickAt(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.press", "CONTROL", "PRESS", controlEvent -> {
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
                        putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                    }
                    eventLog.clear();
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.move(153,-1);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseMove(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                eventLog.clear();
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                    eventLog.add(e);
                }));
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                    if (eventLog.isEmpty()) return;
                    int dx = eventLog.getMouseEventDx();
                    int dy = eventLog.getMouseEventDy();
                    mouseObjectCodeGenerator.move(dx, dy);
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                    eventLog.clear();
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.moveAt(888,651);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseMoveTo(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {

                    mouseObjectCodeGenerator.moveTo(e.getX(), e.getY());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.press('LEFT');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMousePress(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                    mouseObjectCodeGenerator.press(e.getButton());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.pressAt('LEFT',123,446);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMousePressAt(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                    mouseObjectCodeGenerator.pressAt(e.getButton(), e.getX(), e.getY());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.release('LEFT');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseRelease(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                    mouseObjectCodeGenerator.release(e.getButton());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.releaseAt('LEFT',838,450);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseReleaseAt(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                    mouseObjectCodeGenerator.releaseAt(e.getButton(), e.getX(), e.getY());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.release", "CONTROL", "RELEASE", controlEvent -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    /**
     * mouse.wheel('DOWN',3);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseWheel(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.wheel((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    /**
     * mouse.wheelAt('DOWN',3,562,823);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseWheelAt(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.wheelAt((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    /**
     * @param event event from ToggleButton on scene
     */
    /*Combined*/
    @FXML
    void insertCombinedLog(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.combined((code) -> {
                codeTextArea.insertTextIntoCaretPosition(code);
            });
        });
    }

    //
    // TEMPLATES
    //

    private List<Node> templateButtonNodes = new ArrayList<>();

    @FXML
    public VBox keyboardTemplateButtonContainer;

    @FXML
    public VBox languageTemplateButtonContainer;

    @FXML
    public VBox mouseTemplateButtonContainer;

    @FXML
    public VBox systemTemplateButtonContainer;


    /**
     * Initializes template buttons
     * set hints and pasted code to user data from property file
     *
     * @param prop - property file
     */
    private void initTemplateButtons(SourcePropertyFile prop) {
        templateButtonNodes.addAll(keyboardTemplateButtonContainer.getChildren());
        templateButtonNodes.addAll(mouseTemplateButtonContainer.getChildren());
        templateButtonNodes.addAll(languageTemplateButtonContainer.getChildren());
        templateButtonNodes.addAll(systemTemplateButtonContainer.getChildren());
        // Set user data 'String[]' to buttons
        // [0] - hint text
        // [1] - code
        for (Node n : templateButtonNodes) {
            n.setUserData(new String[]{prop.get(n.getId()),
                    prop.get(n.getId() + "Code")});
            n.setOnMouseEntered(this::showCodeSample);
            n.setOnMouseExited(this::hideCodeSample);
        }
    }

    /**
     * inserts template from userData in button
     *
     * @param event - ActionEvent
     */
    @FXML
    public void insertTemplate(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String[] data = (String[]) btn.getUserData();
        if (data == null)
            return;
        putTextIntoCaretPosition(codeTextArea, data[1]);
    }

    /**
     * Shows area with hints and code samples from userData when mouse is under a template button
     *
     * @param event event from mouse hover button
     */
    @FXML
    private void showCodeSample(MouseEvent event) {
        Node btn = (Node) event.getSource();
        String[] data = (String[]) btn.getUserData();
        if (data == null)
            return;
        areaCodeSample.setText(data[0]);
        areaCodeSample.setVisible(true);
    }

    /**
     * Hides hint area
     *
     * @param event - MouseEvent
     */
    @FXML
    private void hideCodeSample(MouseEvent event) {
        areaCodeSample.setVisible(false);
    }

    @FXML
    private ToggleButton btnScriptStatus;

    @FXML
    private ToggleButton btnTogglesStatus;

    @FXML
    private void onBtnStatusScript(ActionEvent event) {
        ToggleButton button = (ToggleButton) event.getSource();
        if (button.isSelected()) {
            runScript();
        } else {
            stopScript();
        }
    }

    /**
     * Invokes by toggle to insert code on main scene
     *
     * @param event actionEvent from toggle
     */
    @FXML
    private void onBtnStatusToggles(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        if (toggle.getUserData() == null) {
            toggle.setSelected(false);
            return;
        } else if (toggle.getUserData() instanceof ToggleButton) {
            ((ToggleButton) toggle.getUserData()).fire();
        }
    }

    private void putTextIntoCaretPosition(TextArea textArea, String text) {
        Platform.runLater(() -> {
            int caretPosition = textArea.getCaretPosition();
            codeTextArea.insertText(caretPosition, text);
        });
    }


    @FXML
    private void onCombinedAbsolutePathAction(ActionEvent event) {
        if (btnCombinedRelativePath.isSelected()) btnCombinedRelativePath.setSelected(false);
    }

    @FXML
    private void onCombinedRelativePathAction(ActionEvent event) {
        if (btnCombinedAbsolutePath.isSelected()) btnCombinedAbsolutePath.setSelected(false);
    }
}
