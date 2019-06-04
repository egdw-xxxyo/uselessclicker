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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.NumberStringConverter;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.WindowManager;
import org.dikhim.jclicker.actions.ShortcutEqualsListener;
import org.dikhim.jclicker.actions.StringPropertyShortcut;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.hotkeys.HotKeys;
import org.dikhim.jclicker.configuration.recordingparams.Combined;
import org.dikhim.jclicker.controllers.utils.recording.EventsRecorder;
import org.dikhim.jclicker.controllers.utils.TemplateButtonGenerator;
import org.dikhim.jclicker.controllers.utils.recording.*;
import org.dikhim.jclicker.jsengine.clickauto.generators.*;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.ui.CodeTextArea;
import org.dikhim.jclicker.ui.LupeImageView;
import org.dikhim.jclicker.ui.OutTextArea;
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
import java.util.prefs.Preferences;

@SuppressWarnings({"unused", "Duplicates", "CodeBlock2Expr", "StringBufferReplaceableByString", "StringConcatenationInLoop"})
public class MainController implements Initializable {

    private Clicker application = Clicker.getApplication();

    private MainApplication mainApplication = Clicker.getApplication().getMainApplication();
    private Preferences preferences = Preferences.userRoot().node(getClass().getName());

    private MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
    private KeyEventsManager keyEventsManager = KeyEventsManager.getInstance();

    private int lineSize = 60;
    private KeyboardObjectOldCodeGenerator keyboardObjectCodeGenerator = new KeyboardObjectOldCodeGenerator(lineSize);
    private MouseObjectOldCodeGenerator mouseObjectCodeGenerator = new MouseObjectOldCodeGenerator(lineSize);
    private SystemObjectOldCodeGenerator systemObjectCodeGenerator = new SystemObjectOldCodeGenerator(lineSize);
    private ClipboardObjectOldCodeGenerator clipboardObjectCodeGenerator = new ClipboardObjectOldCodeGenerator(lineSize);
    private CombinedObjectOldCodeGenerator combinedObjectCodeGenerator = new CombinedObjectOldCodeGenerator(lineSize);

    private EventsRecorder eventsRecorder;
    private MainConfiguration config;


    private ResourceBundle resources;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        config = mainApplication.getConfig();
        // init text areas

        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);
        btnScriptStatus.textProperty().bind(mainApplication.statusProperty());
        btnScriptStatus.selectedProperty().bindBidirectional(mainApplication.getClickAuto().isRunningProperty());

        // code area
        codeTextArea = new CodeTextArea();
        codeAreaPane.getChildren().addAll(codeTextArea);
        codeTextArea.textProperty().bindBidirectional(mainApplication.getScript().codeProperty());

        // output text pane
        OutTextArea outTextArea = new OutTextArea();
        outputTextPane.getChildren().addAll(outTextArea);
        Out.addPrintMethod(outTextArea::appendText);
        Out.addClearMethod(outTextArea::clear);
        outTextArea.addChangeListener(() -> outputTabPane.getSelectionModel().select(0));

        // output image pane
        OutputImageView outputImageView = new OutputImageView(resources);
        outputImageView.setOnInsert(codeTextArea::insertTextIntoCaretPosition);
        outputImageView.setOnLoad(codeTextArea::getSelectedText);
        outputImagePane.getChildren().addAll(outputImageView);
        outputImageView.addChangeListener(() -> outputTabPane.getSelectionModel().select(1));
        mainApplication.setOnSetOutputImage(outputImageView::loadImage);

        // lupe pane
        LupeImageView lupeImageView = new LupeImageView(resources);
        lupePane.getChildren().addAll(lupeImageView);
        lupeImageView.visibleProperty().bind(btnLupeStatus.selectedProperty());


        // events recorder
        eventsRecorder = new EventsRecorder(config);
        eventsRecorder.setOutputTextArea(codeTextArea);

        eventsRecorder.setOnSetOutputImage(outputImageView::loadImage);
        codeTextArea.activeProperty().bind(eventsRecorder.keyboardRecordingProperty().not());

        btnLupeStatus.selectedProperty().bindBidirectional(eventsRecorder.getRecordingStatus().lupeIsNeededProperty());
        btnMouseRecordingStatus.selectedProperty().bindBidirectional(eventsRecorder.getRecordingStatus().activeMouseRecordingProperty());
        btnKeyboardRecordingStatus.selectedProperty().bindBidirectional(eventsRecorder.getRecordingStatus().activeKeyboardRecordingProperty());
        btnRecordingStatus.selectedProperty().bindBidirectional(eventsRecorder.getRecordingStatus().recordingProperty());

        // codesamples file
        SourcePropertyFile propertyFile = new SourcePropertyFile();
        propertyFile.setSource(Resources.getSource(resources.getString("codesamples")));

        initToggles(propertyFile);
        initTemplateButtons(propertyFile);

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
    // status buttons
    @FXML
    private ToggleButton btnScriptStatus;

    @FXML
    private ToggleButton btnActiveRecorderStatus;
    
    @FXML
    private ToggleButton btnLupeStatus;

    @FXML
    private ToggleButton btnMouseRecordingStatus;

    @FXML
    private ToggleButton btnKeyboardRecordingStatus;

    @FXML
    private ToggleButton btnRecordingStatus;
    

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

    private CodeTextArea codeTextArea;

    @FXML
    private TextArea areaCodeSample;
    private StringProperty codeSampleProperty = new SimpleStringProperty("");

    // code pane
    @FXML
    private AnchorPane codeAreaPane;

    // output pane
    @FXML
    private TabPane outputTabPane;

    //// text output
    @FXML
    private AnchorPane outputTextPane;

    //// image output
    @FXML
    private AnchorPane outputImagePane;

    // lupe pane    
    @FXML
    private AnchorPane lupePane;

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

    // Other
    @FXML
    private Button recFilePath;

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

    /**
     * Adds all toggles to listOfInsertCodeToggles and sets hints to user data from property file
     */
    private void initToggles(SourcePropertyFile properties) {
        List<Node> nodes = new ArrayList<>();
        
        // keyboard
        eventsRecorder.bind(btnInsertKeyName, new KeyNameRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertKeyName);

        eventsRecorder.bind(btnInsertKeyCode, new KeyPerformRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertKeyCode);


        eventsRecorder.bind(btnInsertKeyCodeWithDelay, new KeyPerformWithDelaysRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertKeyCodeWithDelay);

        // mouse basics
        eventsRecorder.bind(btnInsertMouseClick, new MouseClickRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseClick);

        eventsRecorder.bind(btnInsertMouseClickAt, new MouseClickAtRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseClickAt);

        eventsRecorder.bind(btnInsertMouseName, new MouseNameRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseName);

        eventsRecorder.bind(btnInsertMouseMoveTo, new MouseMoveToRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseMoveTo);

        eventsRecorder.bind(btnInsertMouseMove, new MouseMoveRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseMove);

        eventsRecorder.bind(btnInsertMousePress, new MousePressRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMousePress);

        eventsRecorder.bind(btnInsertMousePressAt, new MousePressAtRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMousePressAt);

        eventsRecorder.bind(btnInsertMouseRelease, new MouseReleaseRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseRelease);

        eventsRecorder.bind(btnInsertMouseReleaseAt, new MouseReleaseAtRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseReleaseAt);

        eventsRecorder.bind(btnInsertMouseWheel, new WheelRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseWheel);


        // mouse press/release
        eventsRecorder.bind(btnInsertMouseCode, new MouseButtonWheelAtRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseCode);

        eventsRecorder.bind(btnInsertMouseCodeWithDelay, new MouseButtonWheelAtWithDelaysRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseCodeWithDelay);

        eventsRecorder.bind(btnInsertMouseRelativeCode, new MouseMoveAndRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertMouseRelativeCode);

        eventsRecorder.bind(recFilePath, new FilePathRecorder(eventsRecorder::putCode));
        nodes.add(recFilePath);


        // image

        eventsRecorder.bind(btnInsertSelectImage, new ImageRecorder(eventsRecorder::puImage));
        nodes.add(btnInsertSelectImage);

        // combined

        eventsRecorder.bind(btnInsertCombinedLog, new CombinedRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertCombinedLog);
        
        nodes.add(btnCombinedAbsolutePath);
        nodes.add(btnCombinedDelays);
        nodes.add(btnCombinedDetectStopPoints);
        nodes.add(btnCombinedFixRate);
        nodes.add(btnCombinedKeys);
        nodes.add(btnCombinedMinDistance);
        nodes.add(btnCombinedMouseButtons);
        nodes.add(btnCombinedMouseWheel);
        nodes.add(btnCombinedRelativePath);

        // set user data 'String' hint
        nodes.add(combinedEncodingType);
        
        for (Node b : nodes) {
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

    @FXML
    void recFilePath(ActionEvent event) {
        eventsRecorder.filePath();
    }

    //
    // TEMPLATES
    //

    private List<Node> templateButtonNodes = new ArrayList<>();

    @FXML
    public VBox keyboardTemplateButtonContainer;

    @FXML
    public VBox mouseTemplateButtonContainer;

    @FXML
    public VBox clipboardTemplateButtonContainer;

    @FXML
    public VBox systemTemplateButtonContainer;

    @FXML
    public VBox screenTemplateButtonContainer;

    @FXML
    public VBox createTemplateButtonContainer;

    @FXML
    public VBox languageTemplateButtonContainer;


    /**
     * Initializes template buttons
     * set hints and pasted code to user data from property file
     *
     * @param prop - property file
     */
    private void initTemplateButtons(SourcePropertyFile prop) {

        TemplateButtonGenerator buttonGenerator = new TemplateButtonGenerator()
                .setLineSize(120)
                .setProperties(prop)
                .addStyleClass("templateButton")
                .setOnMouseEntered(this::showCodeSample)
                .setOnMouseExited(this::hideCodeSample)
                .setOnAction(this::insertTemplate)
                .build();

        keyboardTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForKeyboardObject());
        mouseTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForMouseObject());
        clipboardTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForClipboardObject());
        systemTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForSystemObject());
        screenTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForScreenObject());
        createTemplateButtonContainer.getChildren().addAll(buttonGenerator.getButtonListForCreateObject());
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
            codeTextArea.insertTextIntoCaretPosition(text);
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
