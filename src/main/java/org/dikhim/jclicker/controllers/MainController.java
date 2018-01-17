package org.dikhim.jclicker.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.dikhim.jclicker.ClickerMain;
import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.events.MouseButtonEvent;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.actions.utils.encoders.UnicodeEncoder;
import org.dikhim.jclicker.jsengine.objects.generators.*;
import org.dikhim.jclicker.model.MainApplication;
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


    @FXML
    private void initialize() {
        // init text areas
        codeTextArea.textProperty().bindBidirectional(mainApplication.getScript().codeProperty());
        outTextArea.textProperty().bindBidirectional(Out.outProperty());
        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);

        btnScriptStatus.textProperty().bind(mainApplication.statusProperty());
        btnScriptStatus.selectedProperty().bindBidirectional(mainApplication.getJse().runningProperty());

        // consume keyboard actions for textArea while variable not true
        codeTextArea.addEventFilter(KeyEvent.KEY_PRESSED,
                e -> {
                    if (!enableCodeType) {
                        e.consume();
                        return;
                    }

                    switch (e.getCode()) {
                        case TAB:
                            String s = "    ";
                            codeTextArea.insertText(codeTextArea.getCaretPosition(), s);
                            e.consume();
                            break;
                        case ENTER:
                            char[] charArray = codeTextArea.getText().toCharArray();
                            int caret = codeTextArea.getCaretPosition();
                            StringBuilder sb = new StringBuilder();
                            for (int i = caret-1; i >= 0; i--) {
                                if (charArray[i] != '\n') {
                                    sb.append(charArray[i]);
                                } else {
                                    break;
                                }
                            }
                            System.out.println("lineSize: "+sb.length());
                            charArray = sb.reverse().toString().toCharArray();
                            sb = new StringBuilder("\n");
                            for (char ch : charArray) {
                                if(ch == ' ') {
                                    sb.append(ch);
                                }else {
                                    break;
                                }
                            }
                            System.out.println("spaces: "+sb.length());
                            codeTextArea.insertText(codeTextArea.getCaretPosition(), sb.toString());
                            e.consume();
                            break;
                    }
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
    private StringProperty codeTextProperty = new SimpleStringProperty();
    private boolean enableCodeType = true;


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

        // movement
        listOfInsertCodeToggles.add(btnInsertAbsolutePath);
        listOfInsertCodeToggles.add(btnInsertAbsolutePathWithDelays);
        listOfInsertCodeToggles.add(btnInsertAbsolutePathFixRate);
        listOfInsertCodeToggles.add(btnInsertRelativePath);
        listOfInsertCodeToggles.add(btnInsertRelativePathWithDelays);
        listOfInsertCodeToggles.add(btnInsertRelativePathFixRate);

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
            deselect(toggleButton);
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

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + ".control.release", "CONTROL", "RELEASE", (e) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        });
    }

    // Movement

    /**
     * mouse.moveAbsolute('㜥㕀㜦㕀㜩㕂㜬㕃㜰㕃');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertAbsolutePath(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getAbsolutePath(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));
        });
    }

    /**
     * mouse.moveAbsolute_D('㝸㖚㓟㝸㖜㐮㝸㖝㐶㝸㖟㐶㝸㖠㐦');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertAbsolutePathWithDelays(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getAbsolutePathWithDelays(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));
        });

    }

    /**
     * mouse.moveRelative('㐦㐦㐦㐨㐨㐪㐧㐧㐦㐨㐦㐧㐦㐧㐧㐧');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertRelativePath(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getRelativePath(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));
        });
    }


    /**
     * mouse.moveRelative_D('㐦㐧㐭㐦㐧㐮㐦㐧㐮㐦㐨㐮㐦㐧㐮㐦㐧㐮㐦㐩㐮㐦㐧㐮㐦㐨㐮㐦㐧㐮㐦㐧㐮㐦㐨㐦');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertRelativePathWithDelays(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(
                    prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

                List<MouseMoveEvent> moveLog = eventLog.getMouseMoveLog();
                MouseMoveEventUtil mouseMoveEventUtil = new MouseMoveEventUtil();
                mouseMoveEventUtil.addAll(moveLog);
                String code = mouseMoveEventUtil.getRelativePathWithDelays(80);
                putTextIntoCaretPosition(codeTextArea, code);
            }));
        });
    }

    /**
     * mouse.moveAbsolute_D('㜬㘾㑮㝊㙁㑥㝵㘵㑦㞡㘥㑮㞭㘎㑦㞾㘋㑦㟘㘝㑦㟱㘶㑮㟿㙆㐦');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertAbsolutePathFixRate(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

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
        });

    }

    /**
     * mouse.moveRelative_D('㐟㐮㑦㐽㐬㑥㐷㐙㑯㐡㐠㑥㐁㑆㐦');
     *
     * @param event event from ToggleButton on scene
     */
    @FXML
    void insertRelativePathFixRate(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".move", (e) -> {
                    eventLog.add(e);
                }));
            }));
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + "control.key.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);

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
    void insertMouseMoveTo(ActionEvent event) {
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
    @FXML
    void insertCombinedLog(ActionEvent event) {
        insertButtonCall(event, prefix -> {
            final boolean[] recording = new boolean[1];
            final String controlKey = txtCombinedControl.getText();
            keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(
                    prefix + ".start", controlKey, "RELEASE", controlEvent -> {

                if (recording[0]) {
                    recording[0] = false;
                    return;
                }
                // start recording on release
                recording[0] = true;
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                keyEventsManager.addKeyboardListener(new ShortcutIncludesListener(prefix + ".keys", "", "", e -> {
                    if (e.getKey().equals(controlKey) && e.getAction().equals("PRESS")) {
                        // stop on press
                        keyEventsManager.removeListenersByPrefix(prefix + ".keys");
                        mouseEventsManager.removeListenersByPrefix(prefix);
                        UnicodeEncoder unicodeEncoder = new UnicodeEncoder();
                        if (btnCombinedKeys.isSelected()) unicodeEncoder.addKeys();
                        if (btnCombinedMouseButtons.isSelected()) unicodeEncoder.addMouseButtons();
                        if (btnCombinedMouseWheel.isSelected()) unicodeEncoder.addMouseWheel();
                        if (btnCombinedAbsolutePath.isSelected()) unicodeEncoder.absolute();
                        if (btnCombinedRelativePath.isSelected()) unicodeEncoder.relative();
                        if (btnCombinedDelays.isSelected()) unicodeEncoder.addDelays();
                        if (btnCombinedFixRate.isSelected())
                            unicodeEncoder.fixedRate(Integer.parseInt(txtCombinedFixRate.getText()));
                        if (btnCombinedMinDistance.isSelected())
                            unicodeEncoder.minDistance(Integer.parseInt(txtCombinedMinDistance.getText()));
                        if (btnCombinedDetectStopPoints.isSelected())
                            unicodeEncoder.detectStopPoints(Integer.parseInt(txtCombinedDetectStopPoints.getText()));
                        String code = unicodeEncoder.encode(eventLog.getEventLog());

                        combinedObjectCodeGenerator.run(code);
                        putTextIntoCaretPosition(codeTextArea, combinedObjectCodeGenerator.getGeneratedCode());
                        eventLog.clear();
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