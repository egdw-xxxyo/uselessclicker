package org.dikhim.jclicker.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dikhim.jclicker.ClickerMain;
import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.events.MouseButtonEvent;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.jsengine.objects.generators.ClipboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.MouseObjectCodeGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.SystemObjectCodeGenerator;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.actions.utils.MouseMoveEventUtil;
import org.dikhim.jclicker.util.output.Out;
import org.dikhim.jclicker.util.SourcePropertyFile;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

@SuppressWarnings({"unused", "Duplicates", "CodeBlock2Expr", "StringBufferReplaceableByString", "StringConcatenationInLoop"})
public class MainController {
    private ClickerMain application;

    private EventLogger eventLog = new EventLogger(10000);

    private MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
    private KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();

    private int lineSize = 60;
    private KeyboardObjectCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator(lineSize);
    private MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator(lineSize);
    private SystemObjectCodeGenerator systemObjectCodeGenerator = new SystemObjectCodeGenerator(lineSize);
    private ClipboardObjectCodeGenerator clipboardObjectCodeGenerator = new ClipboardObjectCodeGenerator(lineSize);

    @FXML
    private void initialize() {
        application = ClickerMain.getApplication();
        // init text areas
        bindOutputProperty();
        bindScriptProperty();

        codeTextArea.textProperty().bindBidirectional(codeTextProperty);
        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);
        areaOut.textProperty().bindBidirectional(outTextProperty);


        // consume keyboard actions for textArea while variable not true
        codeTextArea.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    if (!enableCodeType)
                        event.consume();
                });
        codeTextArea.addEventFilter(KeyEvent.KEY_TYPED,
                event -> {
                    if (!enableCodeType)
                        event.consume();
                });

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

        // Set status suspended and reset toggles status
        setScriptStatus(Status.SUSPENDED);
        setToggleStatus(null);

        KeyEventsManager keyListener = KeyEventsManager.getInstance();
        keyListener.addKeyboardListener(
                new ShortcutEqualsListener("stopScript", "CONTROL ALT S", "PRESS", (e) -> {
                    Platform.runLater(this::stopScript);
                }));
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
    private TextArea codeTextArea;
    private StringProperty codeTextProperty = new SimpleStringProperty("");
    private boolean enableCodeType = true;


    @FXML
    private TextArea areaCodeSample;
    private StringProperty codeSampleProperty = new SimpleStringProperty("");

    @FXML
    private TextArea areaOut;
    private StringProperty outTextProperty = new SimpleStringProperty(" ");

    @FXML
    private TextField txtAbsolutePathRate;

    @FXML
    private TextField txtRelativePathRate;

    private void bindOutputProperty() {
        outTextProperty.bindBidirectional(Out.getStringProperty());
    }

    private void bindScriptProperty() {
        codeTextProperty.bindBidirectional(application.getScript().getStringProperty());
    }

    @FXML
    public void stopScript() {
        application.stopScript();
        setScriptStatus(Status.SUSPENDED);
    }

    @FXML
    public void runScript() {
        Out.clear();
        select(null);
        setScriptStatus(Status.RUNNING);
        application.runScript();
    }

    @FXML
    public void newFile() {
        stopScript();
        application.newScript();
        updateScriptStatus();
    }

    @FXML
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All types", "*.*"),
                new FileChooser.ExtensionFilter("JavaScript", "*.js"));

        Preferences prefs = Preferences.userNodeForPackage(ClickerMain.class);
        String pathFolder = prefs.get("last-opened-folder", null);
        if (pathFolder != null)
            fileChooser.setInitialDirectory(new File(pathFolder));

        File file = fileChooser.showOpenDialog(application.getPrimaryStage());
        if (file != null) {
            Script script = new Script(file);
            application.setScript(script);
            prefs.put("last-opened-folder",
                    file.getParentFile().getAbsolutePath());

            codeTextArea.textProperty()
                    .bindBidirectional(script.getStringProperty());
        }
        updateScriptStatus();
    }

    @FXML
    public void saveFile() {
        Preferences prefs = Preferences.userNodeForPackage(ClickerMain.class);

        Script script = application.getScript();
        if (script == null)
            return;

        if (script.getFile() == null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.setInitialFileName("newFile.js");

            String pathFolder = prefs.get("last-saved-folder", null);
            if (pathFolder != null)
                fileChooser.setInitialDirectory(new File(pathFolder));

            File file = fileChooser.showSaveDialog(application.getPrimaryStage());

            if (file != null) {
                try {
                    FileUtils.writeStringToFile(file,
                            script.getStringProperty().get(),
                            "UTF-8");
                    script.setFile(file);
                    prefs.put("last-saved-folder", script.getFile()
                            .getParentFile().getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            try {
                FileUtils.writeStringToFile(script.getFile(),
                        script.getStringProperty().get(),
                        "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        application.updateTitle();
        updateScriptStatus();
    }

    /**
     * Save script in new file
     */
    @FXML
    public void saveFileAs() {
        System.out.println("Save as");
        Preferences prefs = Preferences.userNodeForPackage(ClickerMain.class);

        Script script = application.getScript();
        if (script == null)
            return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName("newFile.js");

        String pathFolder = prefs.get("last-saved-folder", null);
        if (pathFolder != null)
            fileChooser.setInitialDirectory(new File(pathFolder));

        File file = fileChooser.showSaveDialog(application.getPrimaryStage());

        if (file != null) {
            script.setFile(file);
            try {
                FileUtils.writeStringToFile(script.getFile(),
                        script.getStringProperty().get(),
                        "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        application.updateTitle();
        prefs.put("last-saved-folder",
                script.getFile().getParentFile().getAbsolutePath());
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
    private ToggleButton btnInsertMouseMoveAt;

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

    // Movement
    @FXML
    private ToggleButton btnInsertAbsolutePath;

    @FXML
    private ToggleButton btnInsertRelativePath;

    @FXML
    private ToggleButton btnInsertAbsolutePathWithDelays;

    @FXML
    private ToggleButton btnInsertRelativePathWithDelays;

    @FXML
    private ToggleButton btnInsertAbsolutePathFixRate;

    @FXML
    private ToggleButton btnInsertRelativePathFixRate;


    // Combined
    @FXML
    private ToggleButton btnPressAtReleaseAtDelays;


    private List<ToggleButton> listOfToggles = new ArrayList<>();

    /**
     * Adds all toggles to listOfToggles and sets hints to user data from property file
     */
    private void initToggles(SourcePropertyFile properties) {
        // keyboard
        listOfToggles.add(btnInsertKeyName);
        listOfToggles.add(btnInsertKeyCode);
        listOfToggles.add(btnInsertKeyCodeWithDelay);

        // mouse basics
        listOfToggles.add(btnInsertMouseClick);
        listOfToggles.add(btnInsertMouseClickAt);
        listOfToggles.add(btnInsertMouseName);
        listOfToggles.add(btnInsertMouseMove);
        listOfToggles.add(btnInsertMouseMoveAt);
        listOfToggles.add(btnInsertMousePress);
        listOfToggles.add(btnInsertMousePressAt);
        listOfToggles.add(btnInsertMouseRelease);
        listOfToggles.add(btnInsertMouseReleaseAt);
        listOfToggles.add(btnInsertMouseWheel);

        // mouse press/release
        listOfToggles.add(btnInsertMouseCode);
        listOfToggles.add(btnInsertMouseCodeWithDelay);
        listOfToggles.add(btnInsertMouseRelativeCode);

        //mouse click
        listOfToggles.add(btnInsertMouseCodeClick);

        // movement
        listOfToggles.add(btnInsertAbsolutePath);
        listOfToggles.add(btnInsertAbsolutePathWithDelays);
        listOfToggles.add(btnInsertAbsolutePathFixRate);
        listOfToggles.add(btnInsertRelativePath);
        listOfToggles.add(btnInsertRelativePathWithDelays);
        listOfToggles.add(btnInsertRelativePathFixRate);

        // combined
        listOfToggles.add(btnPressAtReleaseAtDelays);

        // set user data 'String' hint
        for (ToggleButton b : listOfToggles) {
            b.setUserData(properties.get(b.getId()));
        }
    }

    /**
     * Deselect all toggle except the parameter
     *
     * @param selectedToggle - ToggleButton tah one was pressed
     */
    private void select(ToggleButton selectedToggle) {
        for (ToggleButton t : listOfToggles) {
            if (t.isSelected()) {
                if (t != selectedToggle)
                    t.fire();
            }
        }
        setToggleStatus(selectedToggle);
    }

    private void insertButtonCall(ActionEvent event, Consumer<String> consumer) {
        ToggleButton toggleButton = (ToggleButton) event.getSource();
        if (toggleButton == null) return;

        String prefix = toggleButton.getId();
        if (toggleButton.isSelected()) {
            select(toggleButton);
            // if toggle has been selected
            enableCodeType = false;
            consumer.accept(prefix);
        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
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
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".press", "", "PRESS", e -> {
                putTextIntoCaretPosition(codeTextArea, e.getKey() + " ");
            }));
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
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".perform", "", "", (e) -> {
                keyboardObjectCodeGenerator.perform(e.getKey(), e.getAction());
                putTextIntoCaretPosition(codeTextArea, keyboardObjectCodeGenerator.getGeneratedCode());
            }));
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
        insertButtonCall(event, prefix -> {
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
                putTextIntoCaretPosition(codeTextArea, code);
            }));
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
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", (e) -> {
                    mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", (e) -> {
                    mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", "CONTROL", "RELEASE", (e) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
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
        insertButtonCall(event, prefix -> {
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
                    putTextIntoCaretPosition(codeTextArea, code);
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

                    putTextIntoCaretPosition(codeTextArea, code);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.release", "CONTROL", "RELEASE", (e) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
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
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", e -> {
                    if (eventLog.isEmpty()) return;
                    eventLog.add(e);

                    int dx = eventLog.getMouseEventDx();
                    int dy = eventLog.getMouseEventDy();

                    mouseObjectCodeGenerator.moveAndButton(e.getButton(), e.getAction(), dx, dy);
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));

                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                    if (eventLog.isEmpty()) return;
                    eventLog.add(e);

                    int dx = eventLog.getMouseEventDx();
                    int dy = eventLog.getMouseEventDy();

                    mouseObjectCodeGenerator.moveAndWheel(e.getDirection(), e.getAmount(), dx, dy);
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
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
        insertButtonCall(event, prefix -> {
            final int[] a = new int[1];
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                // eventLog must be empty except when PRESS event occurs

                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "", "PRESS", e -> {
                    if (eventLog.getMouseButtonLogSize() > 0) {
                        // if not empty put PRESS code for last event
                        MouseButtonEvent lastE = eventLog.getLastMouseButtonEvent();
                        mouseObjectCodeGenerator.buttonAt(lastE.getButton(), lastE.getAction(), lastE.getX(), lastE.getY());
                        putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                    }
                    eventLog.clear();
                    eventLog.add(e);
                }));

                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "", "RELEASE", e -> {
                    if (eventLog.getMouseButtonLogSize() > 0) {
                        MouseButtonEvent lastE = eventLog.getLastMouseButtonEvent();
                        if (lastE.getButton().equals(e.getButton()) &&
                                lastE.getAction().equals("PRESS") &&
                                lastE.getX() == e.getX() &&
                                lastE.getY() == e.getY()) {
                            // if last event equals to current and it is PRESS event then CLICK
                            mouseObjectCodeGenerator.buttonAt(e.getButton(), "CLICK", e.getX(), e.getY());
                            putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                        } else {
                            // if not CLICK then put two RELEASE event for both events last and current
                            mouseObjectCodeGenerator.buttonAt(lastE.getButton(), lastE.getAction(), lastE.getX(), lastE.getY());
                            putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                            mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                            putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                        }
                    } else {
                        // if eventLog is empty put RELEASE code for current event
                        mouseObjectCodeGenerator.buttonAt(e.getButton(), e.getAction(), e.getX(), e.getY());
                        putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                    }
                    eventLog.clear();
                }));

                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                    if (eventLog.getMouseButtonLogSize() > 0) {
                        // if eventLog is not empty put PRESS code for last event
                        MouseButtonEvent lastE = eventLog.getLastMouseButtonEvent();
                        mouseObjectCodeGenerator.buttonAt(lastE.getButton(), lastE.getAction(), lastE.getX(), lastE.getY());
                        putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                    }
                    // finally put WHEEL code
                    mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
                    putTextIntoCaretPosition(codeTextArea, mouseObjectCodeGenerator.getGeneratedCode());
                    eventLog.clear();
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", "CONTROL", "RELEASE", (e) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    // Movement

    /**
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertAbsolutePath(ActionEvent event) {
        // TODO
        String prefix = "insert.absolute.path";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getAbsolutePath(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));

        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    /**
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertRelativePath(ActionEvent event) {
        // TODO
        String prefix = "insert.relative.path";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getRelativePath(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));

        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    /**
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertAbsolutePathWithDelays(ActionEvent event) {
        // TODO
        String prefix = "insert.relative.path.with.delays";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getAbsolutePathWithDelays(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));

        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    /**
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertRelativePathWithDelays(ActionEvent event) {
        // TODO
        String prefix = "insert.relative.path.with.delays";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getRelativePathWithDelays(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));

        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    /**
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertAbsolutePathFixRate(ActionEvent event) {
        // TODO
        String prefix = "insert.absolute.path.fix.rate";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + "control.key.press", "CONTROL", "PRESS",
                    (controlEvent) -> {
                        eventLog.clear();
                        mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                            eventLog.add(e);
                        }));
                    }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + "control.key.release", "CONTROL", "RELEASE",
                    (controlEvent) -> {
                        mouseEventsManager.removeMoveListenersByPrefix(prefix);

                        List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                        MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                        mouseMoveEventUtil.addAll(moveLog);
                        int fps;
                        try {
                            fps = Integer.parseInt(txtAbsolutePathRate.getText());
                            if (fps < 1) {
                                fps = 1;
                            } else if (fps > 60) {
                                fps = 60;
                            }
                        } catch (Exception ex) {
                            fps = 30;
                        }
                        String code = mouseMoveEventUtil.getAbsolutePathFixRateWithDelays(fps, 80);
                        putTextIntoCaretPosition(codeTextArea, code);


                    }));

        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeMoveListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    /**
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertRelativePathFixRate(ActionEvent event) {
        // TODO
        String prefix = "insert.absolute.path.fix.rate";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + "control.key.press", "CONTROL", "PRESS",
                    (controlEvent) -> {
                        eventLog.clear();
                        mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                            eventLog.add(e);
                        }));
                    }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + "control.key.release", "CONTROL", "RELEASE",
                    (controlEvent) -> {
                        mouseEventsManager.removeMoveListenersByPrefix(prefix);

                        List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                        MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                        mouseMoveEventUtil.addAll(moveLog);

                        int fps;
                        try {
                            fps = Integer.parseInt(txtRelativePathRate.getText());
                            if (fps < 1) {
                                fps = 1;
                            } else if (fps > 60) {
                                fps = 60;
                            }
                        } catch (Exception ex) {
                            fps = 30;
                        }
                        String code = mouseMoveEventUtil.getRelativePathFixRateWithDelays(fps, 80);
                        putTextIntoCaretPosition(codeTextArea, code);


                    }));

        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeMoveListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    // mouse basics

    /**
     * 'LEFT RIGHT MIDDLE '
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseName(ActionEvent event) {
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
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
    void insertMouseMoveAt(ActionEvent event) {
        // TODO rename function name to 'moveTo'
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
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
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                    mouseObjectCodeGenerator.wheel(e.getDirection(), e.getAmount());
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
     * mouse.wheelAt('DOWN',3,562,823);
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertMouseWheelAt(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".control.press", "CONTROL", "PRESS", controlEvent -> {
                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", e -> {
                    mouseObjectCodeGenerator.wheelAt(e.getDirection(), e.getAmount(), e.getX(), e.getY());
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
     * @param event event from ToggleButton on scene
     */
    /*Combined*/
    @SuppressWarnings("Duplicates")
    @FXML
    void insertPressAtReleaseAtDelays(ActionEvent event) {

    }


    /**
     * Shows hint area with text from userData in button
     *
     * @param event - MouseEvent
     */
    @FXML
    public void showCodeSample(MouseEvent event) {
        ToggleButton btn = (ToggleButton) event.getSource();
        areaCodeSample.setText((String) btn.getUserData());
        areaCodeSample.setVisible(true);
    }

    /**
     * Hides hint area
     *
     * @param event - MouseEvent
     */
    @FXML
    public void hideCodeSample(MouseEvent event) {
        areaCodeSample.setVisible(false);
    }

    //// template buttons
    private List<Button> templateButtons = new ArrayList<>();

    @FXML
    private Button btnTemplateMouseClick;

    @FXML
    private Button btnTemplateMouseClickAt;

    @FXML
    private Button btnTemplateMouseGetMoveDelay;

    @FXML
    private Button btnTemplateMouseGetMultiplier;

    @FXML
    private Button btnTemplateMouseGetPressDelay;

    @FXML
    private Button btnTemplateMouseGetReleaseDelay;

    @FXML
    private Button btnTemplateMouseGetSpeed;

    @FXML
    private Button btnTemplateMouseGetWheelDelay;

    @FXML
    private Button btnTemplateMouseGetX;

    @FXML
    private Button btnTemplateMouseGetY;

    @FXML
    private Button btnTemplateMouseMove;

    @FXML
    private Button btnTemplateMouseMoveAbsolute;

    @FXML
    private Button btnTemplateMouseMoveAbsolute_D;

    @FXML
    private Button btnTemplateMouseMoveAndClick;

    @FXML
    private Button btnTemplateMouseMoveAndPress;

    @FXML
    private Button btnTemplateMouseMoveAndRelease;

    @FXML
    private Button btnTemplateMouseMoveAndWheel;

    @FXML
    private Button btnTemplateMouseMoveRelative;

    @FXML
    private Button btnTemplateMouseMoveRelative_D;

    @FXML
    private Button btnTemplateMouseMoveTo;

    @FXML
    private Button btnTemplateMousePress;

    @FXML
    private Button btnTemplateMousePressAt;

    @FXML
    private Button btnTemplateMouseRelease;

    @FXML
    private Button btnTemplateMouseReleaseAt;

    @FXML
    private Button btnTemplateMouseResetDelays;

    @FXML
    private Button btnTemplateMouseResetMultiplier;

    @FXML
    private Button btnTemplateMouseResetSpeed;

    @FXML
    private Button btnTemplateMouseSetDelays;

    @FXML
    private Button btnTemplateMouseSetMoveDelay;

    @FXML
    private Button btnTemplateMouseSetMultiplier;

    @FXML
    private Button btnTemplateMouseSetPressDelay;

    @FXML
    private Button btnTemplateMouseSetReleaseDelay;

    @FXML
    private Button btnTemplateMouseSetSpeed;

    @FXML
    private Button btnTemplateMouseSetWheelDelay;

    @FXML
    private Button btnTemplateMouseSetX;

    @FXML
    private Button btnTemplateMouseSetY;

    @FXML
    private Button btnTemplateMouseWheel;

    @FXML
    private Button btnTemplateMouseWheelAt;

    ////////////// KEYBOARD
    @FXML
    private Button btnTemplateKeyGetMultiplier;

    @FXML
    private Button btnTemplateKeyGetPressDelay;

    @FXML
    private Button btnTemplateKeyGetReleaseDelay;

    @FXML
    private Button btnTemplateKeyGetSpeed;

    @FXML
    private Button btnTemplateKeyIsPressed;

    @FXML
    private Button btnTemplateKeyPress;

    @FXML
    private Button btnTemplateKeyRelease;

    @FXML
    private Button btnTemplateKeyResetDelays;

    @FXML
    private Button btnTemplateKeyResetMultiplier;

    @FXML
    private Button btnTemplateKeyResetSpeed;

    @FXML
    private Button btnTemplateKeyType;

    @FXML
    private Button btnTemplateKeySetDelays;

    @FXML
    private Button btnTemplateKeySetMultiplier;

    @FXML
    private Button btnTemplateKeySetPressDelay;

    @FXML
    private Button btnTemplateKeySetReleaseDelay;

    @FXML
    private Button btnTemplateKeySetSpeed;
    // System
    @FXML
    private Button btnTemplateSystemSleep;

    @FXML
    private Button btnTemplateSystemPrint;

    @FXML
    private Button btnTemplateSystemPrintln;

    @FXML
    private Button btnTemplateSystemRegisterShortcut;

    // Language

    @FXML
    private Button btnTemplateLanguageClass;

    @FXML
    private Button btnTemplateLanguageFunctionVoid;

    @FXML
    private Button btnTemplateLanguageFunctionEcho;

    @FXML
    private Button btnTemplateLanguageLoopForPlus;

    @FXML
    private Button btnTemplateLanguageLoopForMinus;

    @FXML
    private Button btnTemplateLanguageLoopWhile;

    /**
     * Initializes template buttons
     * set hints and pasted code to user data from property file
     *
     * @param prop - property file
     */
    private void initTemplateButtons(SourcePropertyFile prop) {
        // mouse
        templateButtons.add(btnTemplateMouseClick);
        templateButtons.add(btnTemplateMouseClickAt);
        templateButtons.add(btnTemplateMouseGetMoveDelay);
        templateButtons.add(btnTemplateMouseGetMultiplier);
        templateButtons.add(btnTemplateMouseGetPressDelay);
        templateButtons.add(btnTemplateMouseGetReleaseDelay);
        templateButtons.add(btnTemplateMouseGetSpeed);
        templateButtons.add(btnTemplateMouseGetWheelDelay);
        templateButtons.add(btnTemplateMouseGetX);
        templateButtons.add(btnTemplateMouseGetY);
        templateButtons.add(btnTemplateMouseMove);
        templateButtons.add(btnTemplateMouseMoveAbsolute);
        templateButtons.add(btnTemplateMouseMoveAbsolute_D);
        templateButtons.add(btnTemplateMouseMoveAndClick);
        templateButtons.add(btnTemplateMouseMoveAndPress);
        templateButtons.add(btnTemplateMouseMoveAndRelease);
        templateButtons.add(btnTemplateMouseMoveAndWheel);
        templateButtons.add(btnTemplateMouseMoveRelative);
        templateButtons.add(btnTemplateMouseMoveRelative_D);
        templateButtons.add(btnTemplateMouseMoveTo);
        templateButtons.add(btnTemplateMousePress);
        templateButtons.add(btnTemplateMousePressAt);
        templateButtons.add(btnTemplateMouseRelease);
        templateButtons.add(btnTemplateMouseReleaseAt);
        templateButtons.add(btnTemplateMouseResetDelays);
        templateButtons.add(btnTemplateMouseResetMultiplier);
        templateButtons.add(btnTemplateMouseResetSpeed);
        templateButtons.add(btnTemplateMouseSetDelays);
        templateButtons.add(btnTemplateMouseSetMoveDelay);
        templateButtons.add(btnTemplateMouseSetMultiplier);
        templateButtons.add(btnTemplateMouseSetPressDelay);
        templateButtons.add(btnTemplateMouseSetReleaseDelay);
        templateButtons.add(btnTemplateMouseSetSpeed);
        templateButtons.add(btnTemplateMouseSetWheelDelay);
        templateButtons.add(btnTemplateMouseSetX);
        templateButtons.add(btnTemplateMouseSetY);
        templateButtons.add(btnTemplateMouseWheel);
        templateButtons.add(btnTemplateMouseWheelAt);


        // keyboard
        templateButtons.add(btnTemplateKeyGetMultiplier);
        templateButtons.add(btnTemplateKeyGetPressDelay);
        templateButtons.add(btnTemplateKeyGetReleaseDelay);
        templateButtons.add(btnTemplateKeyGetSpeed);
        templateButtons.add(btnTemplateKeyIsPressed);
        templateButtons.add(btnTemplateKeyPress);
        templateButtons.add(btnTemplateKeyRelease);
        templateButtons.add(btnTemplateKeyType);
        templateButtons.add(btnTemplateKeyResetDelays);
        templateButtons.add(btnTemplateKeyResetMultiplier);
        templateButtons.add(btnTemplateKeyResetSpeed);
        templateButtons.add(btnTemplateKeySetDelays);
        templateButtons.add(btnTemplateKeySetMultiplier);
        templateButtons.add(btnTemplateKeySetPressDelay);
        templateButtons.add(btnTemplateKeySetReleaseDelay);
        templateButtons.add(btnTemplateKeySetSpeed);


        // System
        templateButtons.add(btnTemplateSystemSleep);
        templateButtons.add(btnTemplateSystemPrint);
        templateButtons.add(btnTemplateSystemPrintln);
        templateButtons.add(btnTemplateSystemRegisterShortcut);

        //Language
        templateButtons.add(btnTemplateLanguageClass);
        templateButtons.add(btnTemplateLanguageFunctionEcho);
        templateButtons.add(btnTemplateLanguageFunctionVoid);
        templateButtons.add(btnTemplateLanguageLoopForPlus);
        templateButtons.add(btnTemplateLanguageLoopForMinus);
        templateButtons.add(btnTemplateLanguageLoopWhile);

        // Set user data 'String[]' to buttons
        // [0] - hint text
        // [1] - code
        for (Button b : templateButtons) {
            b.setUserData(new String[]{prop.get(b.getId()),
                    prop.get(b.getId() + "Code")});
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
        int caretPosition = codeTextArea.getCaretPosition();
        codeTextArea.insertText(caretPosition, data[1]);

    }

    /**
     * Shows area with hints and code samples from userData when mouse is under a template button
     *
     * @param event event from mouse hover button
     */
    @FXML
    public void showCodeTemplate(MouseEvent event) {
        Button btn = (Button) event.getSource();
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
    public void hideCodeTemplate(MouseEvent event) {
        areaCodeSample.setVisible(false);
    }

    // Status control

    private enum Status {
        RUNNING, SUSPENDED
    }

    @FXML
    private ToggleButton btnScriptStatus;

    @FXML
    private ToggleButton btnTogglesStatus;

    private void setScriptStatus(Status status) {
        btnScriptStatus.setUserData(status);
        if (status == Status.RUNNING) {
            btnScriptStatus.setSelected(true);
            btnScriptStatus
                    .setText("Running:   " + application.getScript().getName());
        } else if (status == Status.SUSPENDED) {
            btnScriptStatus.setSelected(false);
            btnScriptStatus
                    .setText("Suspended: " + application.getScript().getName());
        }
    }

    private void updateScriptStatus() {
        if (btnScriptStatus.isSelected()) {
            btnScriptStatus
                    .setText("Running:   " + application.getScript().getName());
        } else {
            btnScriptStatus
                    .setText("Suspended: " + application.getScript().getName());
        }
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
        title += getTemplateButtonPath(toggle);
        btnTogglesStatus.setText(title);
        btnTogglesStatus.setUserData(toggle);
    }

    private String getTemplateButtonPath(Object button) {
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

    @FXML
    private void onBtnStatusScript(ActionEvent event) {
        ToggleButton button = (ToggleButton) event.getSource();
        if (button.isSelected()) {
            runScript();
        } else {
            stopScript();
        }
    }

    @FXML
    private void onBtnStatusToggles(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        if (toggle.getUserData() == null) {
            toggle.setSelected(false);
            return;
        }
        if (toggle.getUserData() instanceof ToggleButton) {
            ((ToggleButton) toggle.getUserData()).fire();
        }
    }

    private void putTextIntoCaretPosition(TextArea textArea, String text) {
        Platform.runLater(() -> {
            int caretPosition = textArea.getCaretPosition();
            codeTextArea.insertText(caretPosition, text);
        });
    }

}
