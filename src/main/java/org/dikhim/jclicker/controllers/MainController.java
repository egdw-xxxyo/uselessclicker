package org.dikhim.jclicker.controllers;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
import org.dikhim.jclicker.events.*;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.util.MouseMoveEventUtil;
import org.dikhim.jclicker.util.MouseMoveUtil;
import org.dikhim.jclicker.util.output.Out;
import org.dikhim.jclicker.util.SourcePropertyFile;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

@SuppressWarnings({"unused", "Duplicates", "CodeBlock2Expr", "StringBufferReplaceableByString", "StringConcatenationInLoop"})
public class MainController {
    private MouseMoveUtil movementPath;
    private MouseMoveEvent lastMoveEvent;
    private EventLogger eventLog = new EventLogger(10000);
    private ClickerMain application;

    @FXML
    private void initialize() {
        application = ClickerMain.getApplication();
        // init text areas
        bindOutputProperty();
        bindScriptProperty();

        codeTextArea.textProperty().bindBidirectional(codeTextProperty);
        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);
        areaOut.textProperty().bindBidirectional(outTextProperty);


        // consume keyboard events for textArea while variable not true
        codeTextArea.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    if (!enableCodeType)
                        event.consume();
                });
        codeTextArea.addEventFilter(KeyEvent.KEY_TYPED,
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!enableCodeType)
                            event.consume();
                    }
                });

        // init toggles and template buttons
        /*SourcePropertyFile propertyFile = new SourcePropertyFile(
                new File(getClass().getResource("/strings/codesamples_ru.txt")
                        .getFile()));*/
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

        // Set satus suspended and reset toggles status
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

    public void bindOutputProperty() {
        outTextProperty.bindBidirectional(Out.getStringProperty());
    }

    public void bindScriptProperty() {
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
            stage.setScene(new Scene(root, 600, 400));
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

    // Keyboard
    @FXML
    void insertKeyName(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        KeyEventsManager manager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;


            manager.addKeyboardListener(new ShortcutIncludesListener(
                    "insert.keyboard.name", "", "PRESS", (e) -> {
                int caretPosition = codeTextArea.getCaretPosition();
                codeTextArea.insertText(caretPosition,
                        e.getKey() + " ");

            }));
        } else {
            // if toggle has been deselected
            manager.removeListenersByPrefix("insert.keyboard.name");
            enableCodeType = true;
        }
        setToggleStatus(toggle);

    }

    @FXML
    void insertKeyCode(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        KeyEventsManager manager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            manager.addKeyboardListener(new ShortcutIncludesListener(
                    "insert.keyboard.code.press", "", "PRESS", (e) -> {
                int caretPosition = codeTextArea.getCaretPosition();
                codeTextArea.insertText(caretPosition, "key.press('"
                        + e.getKey() + "');\n");

            }));

            manager.addKeyboardListener(new ShortcutIncludesListener(
                    "insert.keyboard.code.release", "", "RELEASE", (e) -> {
                int caretPosition = codeTextArea.getCaretPosition();
                codeTextArea.insertText(caretPosition, "key.release('"
                        + e.getKey() + "');\n");

            }));
        } else {
            // if toggle has been deselected
            manager.removeListenersByPrefix("insert.keyboard.code");
            manager.removeListenersByPrefix("insert.keyboard.code");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertKeyCodeWithDelay(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        KeyEventsManager manager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            manager.clearLog();

            eventLog.clear();
            manager.addKeyboardListener(new ShortcutIncludesListener("insert.keyboard.code.press", "", "PRESS", (e) -> {
                eventLog.add(e);

                StringBuilder sb = new StringBuilder();

                long delay = eventLog.getDelay();
                if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");
                sb.append("key.press('").append(e.getKey()).append("');\n");
                putTextIntoCaretPosition(codeTextArea, sb.toString());
            }));

            manager.addKeyboardListener(new ShortcutIncludesListener("insert.keyboard.code.release", "", "RELEASE", (e) -> {
                eventLog.add(e);

                StringBuilder sb = new StringBuilder();

                long delay = eventLog.getDelay();
                if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");
                sb.append("key.release('").append(e.getKey()).append("');\n");
                putTextIntoCaretPosition(codeTextArea, sb.toString());
            }));
        } else {
            // if toggle has been deselected
            manager.removeListenersByPrefix("insert.keyboard.code");
            manager.removeListenersByPrefix("insert.keyboard.code");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    // Mouse buttons
    @FXML
    void insertMouseCode(ActionEvent event) {
        String prefix = "insert.mouse.code";
        ToggleButton toggle = (ToggleButton) event.getSource();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", (e) -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("mouse.buttonAt('")
                            .append(e.getButton()).append("','")
                            .append(e.getAction()).append("',")
                            .append(e.getX()).append(",")
                            .append(e.getY()).append(");\n");
                    putTextIntoCaretPosition(codeTextArea, sb.toString());
                }));
                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".buttons", "", (e) -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("mouse.wheelAt('")
                            .append(e.getDirection()).append("',")
                            .append(e.getAmount()).append(",")
                            .append(e.getX()).append(",")
                            .append(e.getY()).append(");\n");
                    putTextIntoCaretPosition(codeTextArea, sb.toString());
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.release", "CONTROL", "RELEASE", (e) -> {
                System.out.println("Control released");
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseCodeWithDelay(ActionEvent event) {
        String prefix = "insert.mouse.code.with.delays";
        ToggleButton toggle = (ToggleButton) event.getSource();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                eventLog.add(new MouseMoveEvent(mouseEventsManager.getX(), mouseEventsManager.getY(), System.currentTimeMillis()));

                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".buttons", "", "", (e) -> {
                    org.dikhim.jclicker.events.MouseEvent mouseEvent = eventLog.getLastMouseEvent();
                    eventLog.add(e);
                    StringBuilder sb = new StringBuilder();

                    long delay = e.getTime() - mouseEvent.getTime();
                    if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");

                    sb.append("mouse.buttonAt('")
                            .append(e.getButton()).append("','")
                            .append(e.getAction()).append("',")
                            .append(e.getX()).append(",")
                            .append(e.getY()).append(");\n");
                    putTextIntoCaretPosition(codeTextArea, sb.toString());
                }));
                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", (e) -> {
                    org.dikhim.jclicker.events.MouseEvent mouseEvent = eventLog.getLastMouseEvent();
                    eventLog.add(e);
                    StringBuilder sb = new StringBuilder();

                    long delay = e.getTime() - mouseEvent.getTime();
                    if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");

                    sb.append("mouse.wheelAt('")
                            .append(e.getDirection()).append("',")
                            .append(e.getAmount()).append(",")
                            .append(e.getX()).append(",")
                            .append(e.getY()).append(");\n");
                    putTextIntoCaretPosition(codeTextArea, sb.toString());
                }));
            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.key.release", "CONTROL", "RELEASE", (e) -> {
                System.out.println("Control released");
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));
        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseName(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.press", "PRESS", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append(e.getButton()).append(" ");
                        codeTextArea.insertText(caretPosition, sb.toString());
                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("insert.mouse");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseRelativeCode(ActionEvent event) {
        String prefix = "insert.mouse.relative.code";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // Start record by set the first movement point
            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.press", "CONTROL", "PRESS", (controlEvent) -> {
                eventLog.clear();
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "PRESS", "", e -> {
                    StringBuilder sb = new StringBuilder();
                    int dx, dy;
                    try {
                        org.dikhim.jclicker.events.MouseEvent lastMouseEvent = eventLog.getLastMouseEvent();
                        dx = e.getX() - lastMouseEvent.getX();
                        dy = e.getY() - lastMouseEvent.getY();
                    } catch (IndexOutOfBoundsException ex) {
                        dx = 0;
                        dy = 0;
                    }
                    eventLog.add(e);
                    sb.append("mouse.moveAndPress('")
                            .append(e.getButton()).append("',")
                            .append(dx).append(",")
                            .append(dy).append(");\n");
                    putTextIntoCaretPosition(codeTextArea, sb.toString());
                }));
                mouseEventsManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "RELEASE", "", e -> {
                    StringBuilder sb = new StringBuilder();

                    int dx, dy;
                    try {
                        org.dikhim.jclicker.events.MouseEvent lastMouseEvent = eventLog.getLastMouseEvent();
                        dx = e.getX() - lastMouseEvent.getX();
                        dy = e.getY() - lastMouseEvent.getY();
                    } catch (IndexOutOfBoundsException ex) {
                        dx = 0;
                        dy = 0;
                    }
                    sb.append("mouse.moveAndRelease('")
                            .append(e.getButton()).append("',")
                            .append(dx).append(",")
                            .append(dy).append(");\n");
                    putTextIntoCaretPosition(codeTextArea, sb.toString());
                }));

                mouseEventsManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "", (e) -> {

                }));


            }));

            keyEventsManager.addKeyboardListener(new ShortcutEqualsListener(prefix + ".control.release", "CONTROL", "RELEASE", (controlEvent) -> {
                mouseEventsManager.removeListenersByPrefix(prefix);
            }));


        } else {
            // if toggle has been deselected
            keyEventsManager.removeListenersByPrefix(prefix);
            mouseEventsManager.removeListenersByPrefix(prefix);

            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    // Clicks
    @FXML
    void insertMouseCodeClick(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;

            // insert code on press button
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.press", "PRESS", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();

                        MouseButtonEvent preLastButtonEvent = manager
                                .getPreLastButtonEvent();
                        if (preLastButtonEvent == null)
                            return;
                        if (!e.getAction()
                                .equals(preLastButtonEvent.getAction())) {
                            return;
                        }
                        sb.append("mouse.pressAt('")
                                .append(preLastButtonEvent.getButton())
                                .append("',").append(preLastButtonEvent.getX())
                                .append(",").append(preLastButtonEvent.getY())
                                .append(");\n");
                        codeTextArea.insertText(caretPosition, sb.toString());

                    }));
            // insert code on release button
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.release", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();

                        MouseButtonEvent preLastButtonEvent = manager
                                .getPreLastButtonEvent();
                        if (preLastButtonEvent == null)
                            return;
                        if (e.getButton()
                                .equals(preLastButtonEvent.getButton())) {
                            if (e.getX() == preLastButtonEvent
                                    .getX()
                                    && e
                                    .getY() == preLastButtonEvent
                                    .getY()) {
                                sb.append("mouse.clickAt('")
                                        .append(e.getButton())
                                        .append("',")
                                        .append(e.getX())
                                        .append(",")
                                        .append(e.getY())
                                        .append(");\n");
                            } else {
                                sb.append("mouse.pressAt('")
                                        .append(preLastButtonEvent.getButton())
                                        .append("',")
                                        .append(preLastButtonEvent.getX())
                                        .append(",")
                                        .append(preLastButtonEvent.getY())
                                        .append(");\n");
                                sb.append("mouse.releaseAt('")
                                        .append(e.getButton())
                                        .append("',")
                                        .append(e.getX())
                                        .append(",")
                                        .append(e.getY())
                                        .append(");\n");
                            }

                        } else {

                            if (preLastButtonEvent.getAction()
                                    .equals("PRESS")) {
                                sb.append("mouse.pressAt('")
                                        .append(preLastButtonEvent.getButton())
                                        .append("',")
                                        .append(preLastButtonEvent.getX())
                                        .append(",")
                                        .append(preLastButtonEvent.getY())
                                        .append(");\n");
                            }
                            sb.append("mouse.releaseAt('")
                                    .append(e.getButton())
                                    .append("',").append(e.getX())
                                    .append(",").append(e.getY())
                                    .append(");\n");
                        }
                        codeTextArea.insertText(caretPosition, sb.toString());

                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("insert.mouse");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    // Movement

    @FXML
    void insertAbsolutePath(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.press", "CONTROL", "PRESS",
                            (e) -> {
                                movementPath = new MouseMoveUtil();
                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.release",
                            "CONTROL", "RELEASE", (e) -> {
                        int caretPosition = codeTextArea.getCaretPosition();
                        codeTextArea.insertText(caretPosition, movementPath
                                .getMoveCodeAbsolutePath(80));
                        movementPath = null;
                    }));

            manager.addMoveListener(new MouseMoveHandler("mouse.move", e -> {
                if (movementPath != null) {
                    movementPath.add(e.getX(), e.getY());
                }
            }));
        } else {
            // if toggle has been deselected
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            manager.removeMoveListenersByPrefix("mouse.move");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertRelativePath(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.press", "CONTROL", "PRESS",
                            (e) -> {
                                movementPath = new MouseMoveUtil();
                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.release",
                            "CONTROL", "RELEASE", (e) -> {
                        int caretPosition = codeTextArea.getCaretPosition();
                        codeTextArea.insertText(caretPosition, movementPath
                                .getMoveCodeRelativePath(80));
                        movementPath = null;
                    }));

            manager.addMoveListener(new MouseMoveHandler("mouse.move", e -> {
                MouseMoveEvent preLastMoveEvent = manager.getPreLastMoveEvent();
                if (movementPath != null) {
                    movementPath.add(e.getX() - preLastMoveEvent.getX(),
                            e.getY() - preLastMoveEvent.getY());
                }
            }));
        } else {
            // if toggle has been deselected
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            manager.removeMoveListenersByPrefix("mouse.move");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertAbsolutePathWithDelays(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.press", "CONTROL", "PRESS",
                            (e) -> {
                                //set time of starting record track
                                lastMoveEvent = manager.getPreLastMoveEvent();
                                lastMoveEvent.setTime(System.currentTimeMillis());
                                //new movement path
                                movementPath = new MouseMoveUtil();
                                //add first point
                                movementPath.add(manager.getX(), manager.getY());

                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.release",
                            "CONTROL", "RELEASE", (e) -> {
                        int caretPosition = codeTextArea.getCaretPosition();
                        movementPath.add((int) (System.currentTimeMillis() - lastMoveEvent.getTime()));
                        codeTextArea.insertText(caretPosition, movementPath
                                .getMoveCodeAbsolutePathWithDelays(80));
                        movementPath = null;
                    }));

            manager.addMoveListener(new MouseMoveHandler("mouse.move", e -> {
                if (movementPath != null) {
                    int delay = (int) (e.getTime() - lastMoveEvent.getTime());
                    movementPath.add(delay);
                    int x = e.getX();
                    int y = e.getY();
                    movementPath.add(x, y);
                    lastMoveEvent = e;

                }
            }));
        } else {
            // if toggle has been deselected
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            manager.removeMoveListenersByPrefix("mouse.move");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertRelativePathWithDelays(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // on press key start record path
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.press", "CONTROL", "PRESS",
                            (e) -> {
                                //set time of starting record track
                                lastMoveEvent = manager.getPreLastMoveEvent();
                                lastMoveEvent.setTime(System.currentTimeMillis());
                                //new movement path
                                movementPath = new MouseMoveUtil();
                                //add first point
                                movementPath.add(0, 0);
                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("mouse.move.key.release",
                            "CONTROL", "RELEASE", (e) -> {
                        int caretPosition = codeTextArea.getCaretPosition();
                        movementPath.add((int) (System.currentTimeMillis() - lastMoveEvent.getTime()));

                        codeTextArea.insertText(caretPosition, movementPath
                                .getMoveCodeRelativePathWithDelays(80));
                        movementPath = null;
                    }));

            manager.addMoveListener(new MouseMoveHandler("mouse.move", e -> {
                if (movementPath != null) {
                    int delay = (int) (e.getTime() - lastMoveEvent.getTime());
                    movementPath.add(delay);
                    int dx = e.getX() - lastMoveEvent.getX();
                    int dy = e.getY() - lastMoveEvent.getY();
                    movementPath.add(dx, dy);
                    lastMoveEvent = e;
                }
            }));
        } else {
            // if toggle has been deselected
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("mouse.move");
            manager.removeMoveListenersByPrefix("mouse.move");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertAbsolutePathFixRate(ActionEvent event) {
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

    @FXML
    void insertRelativePathFixRate(ActionEvent event) {
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
    @FXML
    void insertMousePress(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;

            manager.addButtonListener(new MouseButtonHandler("mouse.press", "PRESS", "", e -> {
                if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
                    return;
                int caretPosition = codeTextArea.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.press('")
                        .append(e.getButton())
                        .append("');\n");
                codeTextArea.insertText(caretPosition, sb.toString());
            }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("mouse.press");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMousePressAt(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;

            manager.addButtonListener(new MouseButtonHandler("mouse.press", "PRESS", "", e -> {
                if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
                    return;
                int caretPosition = codeTextArea.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.pressAt('").append(e.getButton()).append("',")
                        .append(e.getX()).append(",").append(e.getY())
                        .append(");\n");
                codeTextArea.insertText(caretPosition, sb.toString());
            }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("mouse.press");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseRelease(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;

            manager.addButtonListener(
                    new MouseButtonHandler("mouse.release", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.release('").append(
                                e.getButton())
                                .append("');\n");
                        codeTextArea.insertText(caretPosition, sb.toString());
                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("mouse.release");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseReleaseAt(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;

            manager.addButtonListener(
                    new MouseButtonHandler("mouse.release", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.releaseAt('").append(e.getButton())
                                .append("',").append(e.getX()).append(",")
                                .append(e.getY()).append(");\n");
                        codeTextArea.insertText(caretPosition, sb.toString());
                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("mouse.release");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }


    @FXML
    void insertMouseWheel(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            manager.addWheelListener(new MouseWheelHandler("mouse.wheel", "", e -> {
                if (!KeyEventsManager.getInstance()
                        .isPressed("CONTROL"))
                    return;
                int caretPosition = codeTextArea.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.wheel('").append(e.getDirection()).append("',").append(e.getAmount()).append(");\n");
                codeTextArea.insertText(caretPosition, sb.toString());
            }));

        } else {
            // if toggle has been deselected
            manager.removeWheelListenersByPrefix("mouse.wheel");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseClick(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;

            manager.addButtonListener(
                    new MouseButtonHandler("mouse.click", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;

                        MouseButtonEvent preLastEvent = manager
                                .getPreLastButtonEvent();
                        if (preLastEvent == null)
                            return;
                        if ((e.getX() != preLastEvent.getX())
                                || (e.getY() != preLastEvent.getY())) {
                            return;
                        }
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.click('").append(e.getButton())
                                .append("');\n");
                        codeTextArea.insertText(caretPosition, sb.toString());
                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("mouse.click");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseClickAt(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;

            manager.addButtonListener(
                    new MouseButtonHandler("mouse.click", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        MouseButtonEvent preLastEvent = manager
                                .getPreLastButtonEvent();
                        if (preLastEvent == null)
                            return;
                        if ((e.getX() != preLastEvent.getX())
                                || (e.getY() != preLastEvent.getY())) {
                            return;
                        }
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.clickAt('")
                                .append(e.getButton()).append("',")
                                .append(e.getX()).append(",")
                                .append(e.getY()).append(");\n");
                        codeTextArea.insertText(caretPosition, sb.toString());
                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("mouse.click");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseMoveAt(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            manager.addButtonListener(new MouseButtonHandler("mouse.press", "PRESS", "", e -> {
                if (!KeyEventsManager.getInstance().isPressed("CONTROL"))
                    return;
                int caretPosition = codeTextArea.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.moveAt(").append(e.getX()).append(",")
                        .append(e.getY()).append(");\n");
                codeTextArea.insertText(caretPosition, sb.toString());
            }));

        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("mouse.press");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseMove(ActionEvent event) {
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("key.press", "CONTROL", "PRESS", (e) -> {
                        lastMoveEvent = manager.getLastMoveEvent();
                    }));
            KeyEventsManager.getInstance().addKeyboardListener(
                    new ShortcutEqualsListener("key.release", "CONTROL", "RELEASE", (e) -> {
                        int caretPosition = codeTextArea.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        int dx = manager.getLastMoveEvent().getX()
                                - lastMoveEvent.getX();
                        int dy = manager.getLastMoveEvent().getY()
                                - lastMoveEvent.getY();
                        sb.append("mouse.move(").append(dx).append(",")
                                .append(dy).append(");\n");
                        codeTextArea.insertText(caretPosition, sb.toString());
                    }));

        } else {
            // if toggle has been deselected
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("key.press");
            KeyEventsManager.getInstance()
                    .removeListenersByPrefix("key.release");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    /*Combined*/
    @SuppressWarnings("Duplicates")
    @FXML
    void insertPressAtReleaseAtDelays(ActionEvent event) {
        String prefix = "insert.press.at.release.at.delays";
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager mouseManager = MouseEventsManager.getInstance();
        KeyEventsManager keyManager = KeyEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            keyManager.addKeyboardListener(new ShortcutEqualsListener("control.key", "CONTROL F12", "RELEASE",
                    (startEvent) -> {
                        if (keyManager.isListenerExistByPrefix(prefix)) {
                            // if exist that means that combination was pressed second time
                            // delete all another listeners and put last event
                            keyManager.removeListenersByPrefix(prefix);
                            mouseManager.removeListenersByPrefix(prefix);
                        } else {
                            // if not then add listeners for events
                            // keyboard
                            keyManager.addKeyboardListener(new ShortcutIncludesListener(prefix + ".press", "", "PRESS",
                                    (e) -> {
                                        eventLog.add(e);

                                        StringBuilder sb = new StringBuilder();

                                        long delay = eventLog.getDelay();
                                        if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");
                                        sb.append("key.press('").append(e.getKey()).append("');\n");
                                        putTextIntoCaretPosition(codeTextArea, sb.toString());
                                    }));
                            keyManager.addKeyboardListener(new ShortcutIncludesListener(prefix + ".release", "", "RELEASE",
                                    (e) -> {
                                        eventLog.add(e);

                                        StringBuilder sb = new StringBuilder();

                                        long delay = eventLog.getDelay();
                                        if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");
                                        sb.append("key.release('").append(e.getKey()).append("');\n");
                                        putTextIntoCaretPosition(codeTextArea, sb.toString());
                                    }));

                            // mouse buttons
                            mouseManager.addButtonListener(new MouseButtonHandler(prefix + ".press", "PRESS", "",
                                    (e) -> {
                                        eventLog.add(e);

                                        StringBuilder sb = new StringBuilder();

                                        long delay = eventLog.getDelay();
                                        if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");
                                        sb.append("mouse.pressAt('")
                                                .append(e.getButton()).append("',")
                                                .append(e.getX()).append(",")
                                                .append(e.getY())
                                                .append(");\n");
                                        putTextIntoCaretPosition(codeTextArea, sb.toString());
                                    }));
                            mouseManager.addButtonListener(new MouseButtonHandler(prefix + ".release", "RELEASE", "",
                                    (e) -> {
                                        eventLog.add(e);

                                        StringBuilder sb = new StringBuilder();

                                        long delay = eventLog.getDelay();
                                        if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");
                                        sb.append("mouse.releaseAt('")
                                                .append(e.getButton()).append("',")
                                                .append(e.getX()).append(",")
                                                .append(e.getY())
                                                .append(");\n");
                                        putTextIntoCaretPosition(codeTextArea, sb.toString());
                                    }));

                            // mouse wheel
                            mouseManager.addWheelListener(new MouseWheelHandler(prefix + ".wheel", "",
                                    (e) -> {
                                        eventLog.add(e);

                                        StringBuilder sb = new StringBuilder();

                                        long delay = eventLog.getDelay();
                                        if (delay > 0) sb.append("system.sleep(").append(delay).append(");\n");
                                        sb.append("mouse.wheel('")
                                                .append(e.getDirection()).append("',")
                                                .append(e.getAmount())
                                                .append(");\n");
                                        putTextIntoCaretPosition(codeTextArea, sb.toString());
                                    }));
                        }
                    }));

        } else {
            // if toggle has been deselected
            keyManager.removeListenersByPrefix("control.key");
            keyManager.removeListenersByPrefix(prefix);
            mouseManager.removeListenersByPrefix(prefix);
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }


    /**
     * Shows hint area with tex from userData in button
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
     * Shows area with hints and sample code from userData in button
     *
     * @param event - MouseEvent
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
        Platform.runLater(()->{
            int caretPosition = textArea.getCaretPosition();
            codeTextArea.insertText(caretPosition, text);
        });
    }

}
