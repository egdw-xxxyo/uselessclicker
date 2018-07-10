package org.dikhim.jclicker.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.dikhim.componentlibrary.components.CodeTextArea;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.WindowManager;
import org.dikhim.jclicker.actions.ShortcutEqualsListener;
import org.dikhim.jclicker.actions.StringPropertyShortcut;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.EventLogger;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.hotkeys.HotKeys;
import org.dikhim.jclicker.configuration.recordingparams.Combined;
import org.dikhim.jclicker.controllers.utils.EventsRecorder;
import org.dikhim.jclicker.controllers.utils.TemplateButtonGenerator;
import org.dikhim.jclicker.jsengine.objects.generators.*;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.ui.OutputImageView;
import org.dikhim.jclicker.util.Converters;
import org.dikhim.jclicker.util.Out;
import org.dikhim.jclicker.util.Resources;
import org.dikhim.jclicker.util.SourcePropertyFile;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

@SuppressWarnings({"unused", "Duplicates", "CodeBlock2Expr", "StringBufferReplaceableByString", "StringConcatenationInLoop"})
public class MainController implements Initializable {

    private Clicker application = Clicker.getApplication();

    private MainApplication mainApplication = Clicker.getApplication().getMainApplication();
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

    private EventsRecorder eventsRecorder;
    private MainConfiguration config;


    private ResourceBundle resources;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        config = mainApplication.getConfig();
        // init text areas
        codeTextArea.textProperty().bindBidirectional(mainApplication.getScript().codeProperty());
        Out.addPrintMethod(outTextArea::appendText);
        Out.addClearMethod(outTextArea::clear);
        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);

        btnScriptStatus.textProperty().bind(mainApplication.statusProperty());
        btnScriptStatus.selectedProperty().bindBidirectional(mainApplication.getJse().runningProperty());

        // output image
        OutputImageView outputImageView = new OutputImageView();
        outputImageView.setOnInsert(codeTextArea::insertTextIntoCaretPosition);
        outputImagePane.getChildren().addAll(outputImageView);
        mainApplication.setOnSetOutputImage(outputImageView::loadImage);


        // events recorder
        eventsRecorder = new EventsRecorder(config);
        eventsRecorder.setOutputTextArea(codeTextArea);
        eventsRecorder.setPreviewPane(previewPane);
        eventsRecorder.setOnSetOutputImage(outputImageView::loadImage);
        // codesamples file
        SourcePropertyFile propertyFile = new SourcePropertyFile();
        propertyFile.setSource(Resources.getSource(resources.getString("codesamples")));
        
        initToggles(propertyFile);
        initTemplateButtons(propertyFile);

        // Init script
        setToggleStatus(null);

        bindConfig();

    }


    private void bindConfig() {
        StringConverter<Number> stringConverter = Converters.getStringToNumberConvertor();


        Combined combined = config.getRecordingParams().getCombined();
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

        combinedEncodingType.setItems(FXCollections.observableArrayList(ActionEncoderFactory.getListOfEncodings()));
        combinedEncodingType.getSelectionModel().select(combined.getEncodingType());
        combinedEncodingType.valueProperty().bindBidirectional(combined.getEncodingTypeValue().valueProperty());

        createHotkeys();
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
    
    @FXML
    private VBox previewPane;
    
    @FXML
    private ImageView outputImage;
    
    @FXML
    private AnchorPane outputImagePane;

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
        select(null);
        mainApplication.runScript();
    }

    @FXML
    public void newFile() {
        mainApplication.newFile();
    }

    @FXML
    public void openFile() {
        File file = WindowManager.getInstance().openScriptFile();

        if (file != null) {
            mainApplication.openFile(file);
        }
    }

    @FXML
    public void saveFile() {
        Script script = mainApplication.getScript();
        if (script.isOpened()) {
            mainApplication.saveFile();
        } else {
            File file = WindowManager.getInstance().saveScriptFileAs();
            if (file != null) {
                mainApplication.saveFileAs(file);
            }
        }
    }

    /**
     * Save script in new file
     */
    @FXML
    public void saveFileAs() {
        File file = WindowManager.getInstance().saveScriptFileAs();
        if (file != null) {
            mainApplication.saveFileAs(file);
        }
    }

    @FXML
    public void showServerWindow() {
        WindowManager.getInstance().showStage("server");
    }

    @FXML
    public void showConfigWindow() {
        WindowManager.getInstance().showStage("settings");
    }

    @FXML
    public void showAboutWindow() {
        WindowManager.getInstance().showStage("about");
    }

    @FXML
    public void showHelpWindow() {
        WindowManager.getInstance().showStage("help");
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
    
    // image
    @FXML
    private ToggleButton btnInsertSelectImage;

    // Combined
    @FXML
    private ToggleButton btnInsertCombinedLog;

    @FXML
    TextField txtCombinedDetectStopPoints;

    @FXML
    TextField txtCombinedFixRate;

    @FXML
    TextField txtCombinedMinDistance;

    @FXML
    ChoiceBox<String> combinedEncodingType;

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

        // image
        listOfInsertCodeToggles.add(btnInsertSelectImage);
        
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

        combinedEncodingType.setUserData(new String[]{properties.get(combinedEncodingType.getId()), ""});
        combinedEncodingType.setOnMouseEntered(this::showCodeSample);
        combinedEncodingType.setOnMouseExited(this::hideCodeSample);
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
                btnTogglesStatus.setText("...");
                btnTogglesStatus.setUserData(null);
                return;
            }
            return;
        }
        btnTogglesStatus.setSelected(toggle.isSelected());
        String title = "";
        if (toggle.isSelected()) {
            title += "Activated: ";
        } else {
            title += " Disabled: ";
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
            eventsRecorder.stopRecording();
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
            eventsRecorder.keyName();

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
            eventsRecorder.keyPerform();
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
            eventsRecorder.keyPerformWithDelays();
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
            eventsRecorder.mouseButtonAndWheelAt();
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
            eventsRecorder.mouseButtonAndWheelAtWithDelays();
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
            eventsRecorder.mouseMoveAndButtonAndWheel();
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
            eventsRecorder.click();
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
            eventsRecorder.insertMouseName();
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
            eventsRecorder.insertMouseClick();
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
            eventsRecorder.insertMouseClickAt();
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
            eventsRecorder.insertMouseMove();
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
            eventsRecorder.insertMouseMoveTo();
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
            eventsRecorder.insertMousePress();
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
            eventsRecorder.insertMousePressAt();
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
            eventsRecorder.insertMouseRelease();
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
            eventsRecorder.insertMouseReleaseAt();
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
            eventsRecorder.wheel();
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
            eventsRecorder.wheelAt();
        });
    }

    /**
     * @param event event from ToggleButton on scene
     */
    /*Combined*/
    @FXML
    void insertCombinedLog(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.combined();
        });
    }

    @FXML
    void insertSelectImage(ActionEvent event) {
        onToggleButtonPerformed(event, prefix -> {
            eventsRecorder.selectImage();
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
        
        TemplateButtonGenerator buttonGenerator = new TemplateButtonGenerator()
                .setLineSize(80)
                .setProperties(prop)
                .addStyleClass("templateButton")
                .setOnMouseEntered(this::showCodeSample)
                .setOnMouseExited(this::hideCodeSample)
                .setOnAction(this::insertTemplate)
                .build();

        keyboardTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForKeyboardObject());
        mouseTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForMouseObject());
        systemTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForSystemObject());
        languageTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForLanguage());
    }

    /**
     * inserts template from userData in button
     *
     * @param event - ActionEvent
     */
    @FXML
    private void insertTemplate(ActionEvent event) {
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


    private void createHotkeys() {

        HotKeys hotKeys = config.getHotKeys();

        KeyEventsManager keyListener = KeyEventsManager.getInstance();

        ShortcutEqualsListener stopScriptListener = new ShortcutEqualsListener();
        StringProperty stopScriptShortcutStringProperty = new SimpleStringProperty("");
        stopScriptShortcutStringProperty.bindBidirectional(hotKeys.getShortcut("stopScript").getKeys().valueProperty());
        stopScriptListener.setName("stopScript");
        stopScriptListener.setShortcut(new StringPropertyShortcut(stopScriptShortcutStringProperty));
        stopScriptListener.setAction("PRESS");
        stopScriptListener.setHandler((e) -> Platform.runLater(this::stopScript));

        ShortcutEqualsListener runScriptListener = new ShortcutEqualsListener();
        StringProperty runScriptShortcutStringProperty = new SimpleStringProperty("");
        runScriptShortcutStringProperty.bindBidirectional(hotKeys.getShortcut("runScript").getKeys().valueProperty());
        runScriptListener.setName("runScript");
        runScriptListener.setShortcut(new StringPropertyShortcut(runScriptShortcutStringProperty));
        runScriptListener.setAction("PRESS");
        runScriptListener.setHandler((e) -> Platform.runLater(this::runScript));


        keyListener.addKeyboardListener(stopScriptListener);
        keyListener.addKeyboardListener(runScriptListener);
    }
   
}
