package org.dikhim.jclicker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import javafx.application.Application;
import org.apache.commons.io.FileUtils;
import org.dikhim.jclicker.events.*;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.util.Cli;
import org.dikhim.jclicker.util.MouseMoveUtil;
import org.dikhim.jclicker.util.output.Out;
import org.dikhim.jclicker.util.SourcePropertyFile;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class MainController {
    private MouseMoveUtil movementPath;
    private MouseMoveEvent lastMoveEvent;
    private ClickerMain application;

    @FXML
    private void initialize() {
        application = ClickerMain.getApplication();
        // init textareas
        areaCode.textProperty().bindBidirectional(codeTextProperty);
        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);
        areaOut.textProperty().bindBidirectional(outTextProperty);

        // consume keyboard events for textArea while variable not true
        areaCode.addEventFilter(KeyEvent.KEY_PRESSED,
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!enableCodeType)
                            event.consume();
                    }
                });
        areaCode.addEventFilter(KeyEvent.KEY_TYPED,
                new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!enableCodeType)
                            event.consume();
                    }
                });

        // init toggles and template buttons
        SourcePropertyFile propertyFile = new SourcePropertyFile(
                new File(getClass().getResource("/strings/codesamples_ru.txt")
                        .getFile()));
        initToggles(propertyFile);
        initTemplateButtons(propertyFile);

        // Init script

        // Set satus suspended and reset toggles status
        setScriptStatus(Status.SUSPENDED);
        setToggleStatus(null);
    }

    @FXML
    private Button btnRunScript;
    @FXML
    private Button btnStopScript;
    @FXML
    private Button btnNewFile;
    @FXML
    private Button btnOpenFile;
    @FXML
    private Button btnSaveFile;

    @FXML
    private TextArea areaCode;
    private StringProperty codeTextProperty = new SimpleStringProperty("");
    private boolean enableCodeType = true;

    @FXML
    private TextArea areaCodeSample;
    private StringProperty codeSampleProperty = new SimpleStringProperty("");

    @FXML
    private TextArea areaOut;
    private StringProperty outTextProperty = Out.getProperty();

    public void bindOutputProperty(StringProperty property){
        outTextProperty.bindBidirectional(property);
    }

    public void bindScriptProperty(StringProperty property){
        codeTextProperty.bindBidirectional(property);
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

            areaCode.textProperty()
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
                            Charset.defaultCharset());
                    script.setFile(file);
                    prefs.put("last-saved-folder", script.getFile()
                            .getParentFile().getAbsolutePath());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        } else {
            try {
                FileUtils.writeStringToFile(script.getFile(),
                        script.getStringProperty().get(),
                        Charset.defaultCharset());
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
                        Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        application.updateTitle();
        prefs.put("last-saved-folder",
                script.getFile().getParentFile().getAbsolutePath());
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
        listOfToggles.add(btnInsertRelativePath);
        listOfToggles.add(btnInsertRelativePathWithDelays);

        // set user data 'String' hint
        for (ToggleButton b : listOfToggles) {
            b.setUserData(properties.get(b.getId()));
        }
    }

    /**
     * Deselect all toggle except the parameter
     *
     * @param selectedToggle
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
            manager.addPressListener(new ShortcutIncludesHandler(
                    "insert.keyboard.name", "", () -> {
                int caretPosition = areaCode.getCaretPosition();
                areaCode.insertText(caretPosition,
                        manager.getLastPressed() + " ");

            }));
        } else {
            // if toggle has been deselected
            manager.removePressListenersByPrefix("insert.keyboard.name");
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
            manager.addPressListener(new ShortcutIncludesHandler(
                    "insert.keyboard.code.press", "", () -> {
                int caretPosition = areaCode.getCaretPosition();
                areaCode.insertText(caretPosition, "key.press('"
                        + manager.getLastPressed() + "');\n");

            }));

            manager.addReleaseListener(new ShortcutIncludesHandler(
                    "insert.keyboard.code.release", "", () -> {
                int caretPosition = areaCode.getCaretPosition();
                areaCode.insertText(caretPosition, "key.release('"
                        + manager.getLastReleased() + "');\n");

            }));
        } else {
            // if toggle has been deselected
            manager.removePressListenersByPrefix("insert.keyboard.code");
            manager.removeReleaseListenersByPrefix("insert.keyboard.code");
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
            manager.resetTimeLog();
            manager.addPressListener(new ShortcutIncludesHandler(
                    "insert.keyboard.code.press", "", () -> {
                int caretPosition = areaCode.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                int delay = manager.getLastDelay();
                if (delay != 0) {
                    sb.append("system.sleep(").append(delay)
                            .append(");\n");
                }
                sb.append("key.press('")
                        .append(manager.getLastPressed())
                        .append("');\n");
                areaCode.insertText(caretPosition, sb.toString());

            }));
            manager.addReleaseListener(new ShortcutIncludesHandler(
                    "insert.keyboard.code.release", "", () -> {
                int caretPosition = areaCode.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                int delay = manager.getLastDelay();
                if (delay != 0) {
                    sb.append("system.sleep(").append(delay)
                            .append(");\n");
                }
                sb.append("key.release('")
                        .append(manager.getLastReleased())
                        .append("');\n");
                areaCode.insertText(caretPosition, sb.toString());

            }));
        } else {
            // if toggle has been deselected
            manager.removePressListenersByPrefix("insert.keyboard.code");
            manager.removeReleaseListenersByPrefix("insert.keyboard.code");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    // Mouse buttons
    @FXML
    void insertMouseCode(ActionEvent event) {
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
                        int caretPosition = areaCode.getCaretPosition();

                        areaCode.insertText(caretPosition,
                                "mouse.pressAt('" + e.getButton()
                                        + "'," + e.getX() + ","
                                        + e.getY() + ");\n");
                    }));
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.release", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = areaCode.getCaretPosition();

                        areaCode.insertText(caretPosition,
                                "mouse.releaseAt('" + e.getButton()
                                        + "'," + e.getX() + ","
                                        + e.getY() + ");\n");
                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("insert.mouse");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    @FXML
    void insertMouseCodeWithDelay(ActionEvent event) {
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
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();

                        MouseButtonEvent preLast = manager
                                .getPreLastButtonEvent();
                        int delay = (int) (e.getTime() - preLast.getTime());
                        if (delay != 0) {
                            sb.append("system.sleep(").append(delay)
                                    .append(");\n");
                        }
                        sb.append("mouse.pressAt('")
                                .append(e.getButton()).append("',")
                                .append(e.getX()).append(",")
                                .append(e.getY()).append(");\n");
                        areaCode.insertText(caretPosition, sb.toString());


                    }));
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.release", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        MouseButtonEvent last = manager.getLastButtonEvent();
                        MouseButtonEvent preLast = manager
                                .getPreLastButtonEvent();
                        int delay = (int) (e.getTime() - preLast.getTime());
                        if (delay != 0) {
                            sb.append("system.sleep(").append(delay)
                                    .append(");\n");
                        }
                        sb.append("mouse.releaseAt('")
                                .append(e.getButton()).append("',")
                                .append(e.getX()).append(",")
                                .append(e.getY()).append(");\n");
                        areaCode.insertText(caretPosition, sb.toString());
                    }));
        } else {
            // if toggle has been deselected
            manager.removeButtonListenersByPrefix("insert.mouse");
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
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append(e.getButton()).append(" ");
                        areaCode.insertText(caretPosition, sb.toString());
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
        ToggleButton toggle = (ToggleButton) event.getSource();
        MouseEventsManager manager = MouseEventsManager.getInstance();
        if (toggle.isSelected()) {
            select(toggle);
            // if toggle has been selected
            enableCodeType = false;
            // Start record by set the first movement point
            KeyEventsManager.getInstance().addPressListener(
                    new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
                            () -> {
                                lastMoveEvent = manager.getLastMoveEvent();
                            }));

            // insert code on press button
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.press", "PRESS", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();

                        int dx = (int) (e.getX()
                                - lastMoveEvent.getX());
                        int dy = (int) (e.getY()
                                - lastMoveEvent.getY());
                        lastMoveEvent = manager.getLastMoveEvent();
                        sb.append("mouse.moveAndPress('")
                                .append(e.getButton())
                                .append("',").append(dx).append(",").append(dy)
                                .append(");\n");
                        areaCode.insertText(caretPosition, sb.toString());

                    }));
            // insert code on release button
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.release", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();

                        int dx = (int) (e.getX()
                                - lastMoveEvent.getX());
                        int dy = (int) (e.getY()
                                - lastMoveEvent.getY());
                        lastMoveEvent = manager.getLastMoveEvent();
                        sb.append("mouse.moveAndRelease('")
                                .append(e.getButton())
                                .append("',").append(dx).append(",").append(dy)
                                .append(");\n");
                        areaCode.insertText(caretPosition, sb.toString());

                    }));
            // Past code on release controll key
            KeyEventsManager.getInstance().addReleaseListener(
                    new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
                            () -> {
                                int caretPosition = areaCode.getCaretPosition();
                                StringBuilder sb = new StringBuilder();
                                MouseMoveEvent moveEvent = manager
                                        .getLastMoveEvent();
                                if (moveEvent == null)
                                    return;
                                int dx = (int) (moveEvent.getX()
                                        - lastMoveEvent.getX());
                                int dy = (int) (moveEvent.getY()
                                        - lastMoveEvent.getY());

                                sb.append("mouse.move('").append(dx).append(",")
                                        .append(dy).append(");\n");
                                areaCode.insertText(caretPosition,
                                        sb.toString());
                            }));
        } else {
            // if toggle has been deselected
            KeyEventsManager.getInstance()
                    .removePressListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeReleaseListenersByPrefix("mouse.move");
            manager.removeButtonListenersByPrefix("insert.mouse");

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
                        int caretPosition = areaCode.getCaretPosition();
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
                        areaCode.insertText(caretPosition, sb.toString());

                    }));
            // insert code on release button
            manager.addButtonListener(
                    new MouseButtonHandler("insert.mouse.release", "RELEASE", "", e -> {
                        if (!KeyEventsManager.getInstance()
                                .isPressed("CONTROL"))
                            return;
                        int caretPosition = areaCode.getCaretPosition();
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
                        areaCode.insertText(caretPosition, sb.toString());

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
            KeyEventsManager.getInstance().addPressListener(
                    new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
                            () -> {
                                movementPath = new MouseMoveUtil();
                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addReleaseListener(
                    new ShortcutEqualsHandler("mouse.move.key.release",
                            "CONTROL", () -> {
                        int caretPosition = areaCode.getCaretPosition();
                        areaCode.insertText(caretPosition, movementPath
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
                    .removePressListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeReleaseListenersByPrefix("mouse.move");
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
            KeyEventsManager.getInstance().addPressListener(
                    new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
                            () -> {
                                movementPath = new MouseMoveUtil();
                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addReleaseListener(
                    new ShortcutEqualsHandler("mouse.move.key.release",
                            "CONTROL", () -> {
                        int caretPosition = areaCode.getCaretPosition();
                        areaCode.insertText(caretPosition, movementPath
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
                    .removePressListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeReleaseListenersByPrefix("mouse.move");
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
            KeyEventsManager.getInstance().addPressListener(
                    new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
                            () -> {
                                //set time of starting record track
                                lastMoveEvent = manager.getPreLastMoveEvent();
                                lastMoveEvent.setTime(System.currentTimeMillis());
                                //new movement path
                                movementPath = new MouseMoveUtil();
                                //add first point
                                movementPath.add(manager.getX(), manager.getY());

                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addReleaseListener(
                    new ShortcutEqualsHandler("mouse.move.key.release",
                            "CONTROL", () -> {
                        int caretPosition = areaCode.getCaretPosition();
                        movementPath.add((int) (System.currentTimeMillis() - lastMoveEvent.getTime()));
                        areaCode.insertText(caretPosition, movementPath
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
                    .removePressListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeReleaseListenersByPrefix("mouse.move");
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
            KeyEventsManager.getInstance().addPressListener(
                    new ShortcutEqualsHandler("mouse.move.key.press", "CONTROL",
                            () -> {
                                //set time of starting record track
                                lastMoveEvent = manager.getPreLastMoveEvent();
                                lastMoveEvent.setTime(System.currentTimeMillis());
                                //new movement path
                                movementPath = new MouseMoveUtil();
                                //add first point
                                movementPath.add(0, 0);
                            }));
            // on release key stop record, insert code and clear path;
            KeyEventsManager.getInstance().addReleaseListener(
                    new ShortcutEqualsHandler("mouse.move.key.release",
                            "CONTROL", () -> {
                        int caretPosition = areaCode.getCaretPosition();
                        movementPath.add((int) (System.currentTimeMillis() - lastMoveEvent.getTime()));

                        areaCode.insertText(caretPosition, movementPath
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
                    .removePressListenersByPrefix("mouse.move");
            KeyEventsManager.getInstance()
                    .removeReleaseListenersByPrefix("mouse.move");
            manager.removeMoveListenersByPrefix("mouse.move");
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
                int caretPosition = areaCode.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.press('")
                        .append(e.getButton())
                        .append("');\n");
                areaCode.insertText(caretPosition, sb.toString());
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
                int caretPosition = areaCode.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.pressAt('").append(e.getButton()).append("',")
                        .append(e.getX()).append(",").append(e.getY())
                        .append(");\n");
                areaCode.insertText(caretPosition, sb.toString());
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
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.release('").append(
                                e.getButton())
                                .append("');\n");
                        areaCode.insertText(caretPosition, sb.toString());
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
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.releaseAt('").append(e.getButton())
                                .append("',").append(e.getX()).append(",")
                                .append(e.getY()).append(");\n");
                        areaCode.insertText(caretPosition, sb.toString());
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
                int caretPosition = areaCode.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.wheel('").append(e.getDirection()).append("',").append(e.getAmount()).append(");\n");
                areaCode.insertText(caretPosition, sb.toString());
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
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.click('").append(e.getButton())
                                .append("');\n");
                        areaCode.insertText(caretPosition, sb.toString());
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
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        sb.append("mouse.clickAt('")
                                .append(e.getButton()).append("',")
                                .append(e.getX()).append(",")
                                .append(e.getY()).append(");\n");
                        areaCode.insertText(caretPosition, sb.toString());
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
                int caretPosition = areaCode.getCaretPosition();
                StringBuilder sb = new StringBuilder();
                sb.append("mouse.moveAt(").append(e.getX()).append(",")
                        .append(e.getY()).append(");\n");
                areaCode.insertText(caretPosition, sb.toString());
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
            KeyEventsManager.getInstance().addPressListener(
                    new ShortcutEqualsHandler("key.press", "CONTROL", () -> {
                        lastMoveEvent = manager.getLastMoveEvent();
                    }));
            KeyEventsManager.getInstance().addReleaseListener(
                    new ShortcutEqualsHandler("key.release", "CONTROL", () -> {
                        int caretPosition = areaCode.getCaretPosition();
                        StringBuilder sb = new StringBuilder();
                        int dx = manager.getLastMoveEvent().getX()
                                - lastMoveEvent.getX();
                        int dy = manager.getLastMoveEvent().getY()
                                - lastMoveEvent.getY();
                        sb.append("mouse.move(").append(dx).append(",")
                                .append(dy).append(");\n");
                        areaCode.insertText(caretPosition, sb.toString());
                    }));

        } else {
            // if toggle has been deselected
            KeyEventsManager.getInstance()
                    .removePressListenersByPrefix("key.press");
            KeyEventsManager.getInstance()
                    .removeReleaseListenersByPrefix("key.release");
            enableCodeType = true;
        }
        setToggleStatus(toggle);
    }

    /**
     * Shows hint area with tex from userData in button
     *
     * @param event
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
     * @param event
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
    private Button btnTemplateMouseGetPressDelay;

    @FXML
    private Button btnTemplateMouseGetReleaseDelay;

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
    private Button btnTemplateMouseSetMoveDelay;

    @FXML
    private Button btnTemplateMouseSetPressDelay;

    @FXML
    private Button btnTemplateMouseSetReleaseDelay;

    @FXML
    private Button btnTemplateMouseSetWheelDelay;

    @FXML
    private Button btnTemplateMouseSetX;

    @FXML
    private Button btnTemplateMouseSetY;

    @FXML
    private Button btnTemplateMouseWheel;

    ////////////// KEYBOARD
    @FXML
    private Button btnTemplateKeyGetPressDelay;

    @FXML
    private Button btnTemplateKeyGetReleaseDelay;

    @FXML
    private Button btnTemplateKeyIsPressed;

    @FXML
    private Button btnTemplateKeyPress;

    @FXML
    private Button btnTemplateKeyRelease;

    @FXML
    private Button btnTemplateKeyType;

    @FXML
    private Button btnTemplateKeySetPressDelay;

    @FXML
    private Button btnTemplateKeySetReleaseDelay;
    // System
    @FXML
    private Button btnTemplateSystemSleep;

    @FXML
    private Button btnTemplateSystemPrint;

    @FXML
    private Button btnTemplateSystemPrintln;

    @FXML
    private Button btnTemplateSystemRegisterShortcut;

    /**
     * Initializes template buttons
     * set hints and pasted code to user data from property file
     *
     * @param prop
     */
    private void initTemplateButtons(SourcePropertyFile prop) {
        // mouse
        templateButtons.add(btnTemplateMouseClick);
        templateButtons.add(btnTemplateMouseClickAt);
        templateButtons.add(btnTemplateMouseGetMoveDelay);
        templateButtons.add(btnTemplateMouseGetPressDelay);
        templateButtons.add(btnTemplateMouseGetReleaseDelay);
        templateButtons.add(btnTemplateMouseGetWheelDelay);
        templateButtons.add(btnTemplateMouseGetX);
        templateButtons.add(btnTemplateMouseGetY);
        templateButtons.add(btnTemplateMouseMove);
        templateButtons.add(btnTemplateMouseMoveAbsolute);
        templateButtons.add(btnTemplateMouseMoveAbsolute_D);
        templateButtons.add(btnTemplateMouseMoveAndClick);
        templateButtons.add(btnTemplateMouseMoveAndPress);
        templateButtons.add(btnTemplateMouseMoveAndRelease);
        templateButtons.add(btnTemplateMouseMoveRelative);
        templateButtons.add(btnTemplateMouseMoveRelative_D);
        templateButtons.add(btnTemplateMouseMoveTo);
        templateButtons.add(btnTemplateMousePress);
        templateButtons.add(btnTemplateMousePressAt);
        templateButtons.add(btnTemplateMouseRelease);
        templateButtons.add(btnTemplateMouseReleaseAt);
        templateButtons.add(btnTemplateMouseSetMoveDelay);
        templateButtons.add(btnTemplateMouseSetPressDelay);
        templateButtons.add(btnTemplateMouseSetReleaseDelay);
        templateButtons.add(btnTemplateMouseSetWheelDelay);
        templateButtons.add(btnTemplateMouseSetX);
        templateButtons.add(btnTemplateMouseSetY);
        templateButtons.add(btnTemplateMouseWheel);

        // keyboard
        templateButtons.add(btnTemplateKeyGetPressDelay);
        templateButtons.add(btnTemplateKeyGetReleaseDelay);
        templateButtons.add(btnTemplateKeyIsPressed);
        templateButtons.add(btnTemplateKeyPress);
        templateButtons.add(btnTemplateKeyRelease);
        templateButtons.add(btnTemplateKeyType);
        templateButtons.add(btnTemplateKeySetPressDelay);
        templateButtons.add(btnTemplateKeySetReleaseDelay);

        // System
        templateButtons.add(btnTemplateSystemSleep);
        templateButtons.add(btnTemplateSystemPrint);
        templateButtons.add(btnTemplateSystemPrintln);
        templateButtons.add(btnTemplateSystemRegisterShortcut);

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
     * @param event
     */
    @FXML
    public void insertTemplate(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String[] data = (String[]) btn.getUserData();
        if (data == null)
            return;
        int caretPosition = areaCode.getCaretPosition();
        areaCode.insertText(caretPosition, data[1]);

    }

    /**
     * Shows area with hints and sample code from userData in button
     *
     * @param event
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
     * @param event
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

}
