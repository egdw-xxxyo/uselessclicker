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
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.WindowManager;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.configuration.hotkeys.HotKeys;
import org.dikhim.jclicker.configuration.storage.CombinedRecordingParams;
import org.dikhim.jclicker.controllers.utils.TemplateButtonGenerator;
import org.dikhim.jclicker.controllers.utils.recording.*;
import org.dikhim.jclicker.eventmanager.listener.ShortcutPressListener;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.ui.CodeTextArea;
import org.dikhim.jclicker.ui.LupeImageView;
import org.dikhim.jclicker.ui.OutTextArea;
import org.dikhim.jclicker.ui.OutputImageView;
import org.dikhim.jclicker.util.Converters;
import org.dikhim.jclicker.util.FormattedProperties;
import org.dikhim.jclicker.util.Out;
import org.dikhim.jclicker.util.Resources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private Clicker application = Clicker.getApplication();

    private MainApplication mainApplication = Clicker.getApplication().getMainApplication();

    private EventsRecorder eventsRecorder;


    private ResourceBundle resources;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;
        // init text areas

        areaCodeSample.textProperty().bindBidirectional(codeSampleProperty);

        initMenuButtons();

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
        eventsRecorder = new EventsRecorder();
        eventsRecorder.setOutputTextArea(codeTextArea);

        eventsRecorder.setOnSetOutputImage(outputImageView::loadImage);
        codeTextArea.activeProperty().bind(eventsRecorder.keyboardRecordingProperty().not());

        btnLupeStatus.selectedProperty().bindBidirectional(eventsRecorder.getRecordingStatus().lupeIsNeededProperty());
        btnMouseRecordingStatus.selectedProperty().bind(eventsRecorder.getRecordingStatus().activeMouseRecordingProperty());
        btnKeyboardRecordingStatus.selectedProperty().bind(eventsRecorder.getRecordingStatus().activeKeyboardRecordingProperty());
        btnRecordingStatus.selectedProperty().bindBidirectional(eventsRecorder.getRecordingStatus().recordingProperty());
        lblControl.textProperty().bind(eventsRecorder.getRecordingStatus().controlKeyRequiredProperty());

        // left toggle run/stop button
        lblStopShortcut.textProperty().bind(Dependency.getConfig().hotKeys().runScript().keysProperty());
        btnScriptStatus.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                lblStopShortcut.textProperty().unbind();
                lblStopShortcut.textProperty().bind(Dependency.getConfig().hotKeys().stopScript().keysProperty());
            } else {
                lblStopShortcut.textProperty().unbind();
                lblStopShortcut.textProperty().bind(Dependency.getConfig().hotKeys().runScript().keysProperty());
            }
        });
        btnScriptStatus.selectedProperty().bindBidirectional(mainApplication.getClickAuto().isRunningProperty());

        eventsRecorder.addActiveRecorderToggleButton(btnActiveRecorderStatus);


        // codeSamples file
        FormattedProperties codeSamplesProperties = new FormattedProperties();
        try {
            codeSamplesProperties.load(Resources.getInputStream(resources.getString("codeSamples")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        initToggles(codeSamplesProperties);
        initTemplateButtons(codeSamplesProperties);

        bindConfig();
    }


    private void bindConfig() {
        StringConverter<Number> stringConverter = Converters.getStringToNumberConvertor();


        CombinedRecordingParams combinedRecordingParams = Dependency.getConfig().storage().combinedRecordingParams();

        Bindings.bindBidirectional(txtCombinedFixRate.textProperty(), combinedRecordingParams.fixedRateProperty(), stringConverter);
        Bindings.bindBidirectional(txtCombinedMinDistance.textProperty(), combinedRecordingParams.minDistanceProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtCombinedDetectStopPoints.textProperty(), combinedRecordingParams.stopDetectionTimeProperty(), new NumberStringConverter());

        Bindings.bindBidirectional(btnCombinedDelays.selectedProperty(), combinedRecordingParams.includeDelaysProperty());
        Bindings.bindBidirectional(btnCombinedKeys.selectedProperty(), combinedRecordingParams.includeKeyboardProperty());
        Bindings.bindBidirectional(btnCombinedMouseButtons.selectedProperty(), combinedRecordingParams.includeMouseButtonsProperty());
        Bindings.bindBidirectional(btnCombinedMouseWheel.selectedProperty(), combinedRecordingParams.includeMouseWheelProperty());
        Bindings.bindBidirectional(btnCombinedAbsolutePath.selectedProperty(), combinedRecordingParams.absoluteProperty());
        Bindings.bindBidirectional(btnCombinedRelativePath.selectedProperty(), combinedRecordingParams.relativeProperty());

        Bindings.bindBidirectional(btnCombinedFixRate.selectedProperty(), combinedRecordingParams.fixedRateOnProperty());
        Bindings.bindBidirectional(btnCombinedMinDistance.selectedProperty(), combinedRecordingParams.minDistanceOnProperty());
        Bindings.bindBidirectional(btnCombinedDetectStopPoints.selectedProperty(), combinedRecordingParams.stopDetectionOnProperty());

        combinedEncodingType.setItems(FXCollections.observableArrayList(ActionEncoderFactory.getListOfEncodings()));
        combinedEncodingType.getSelectionModel().select(combinedRecordingParams.getEncodingType());
        combinedEncodingType.valueProperty().bindBidirectional(combinedRecordingParams.encodingTypeProperty());

//        createHotkeys();
    }


    //////////////////////////////////////////////////////////////
    // MENU BUTTONS
    //////////////////////////////////////////////////////////////

    @FXML
    private Button btnNewFile;

    @FXML
    private Button btnOpenFile;

    @FXML
    private Button btnSaveFile;

    @FXML
    private Button btnSaveFileAs;

    @FXML
    private Button btnRunScript;

    @FXML
    private Button btnStopScript;

    @FXML
    private Button btnShowSeverWindow;

    @FXML
    private Button btnShowConfigWindow;

    private void initMenuButtons() {
        btnNewFile.setOnAction(event -> mainApplication.newFile());

        btnOpenFile.setOnAction(event -> {
            File file = WindowManager.getInstance().openScriptFile();

            if (file != null) {
                mainApplication.openFile(file);
            }
        });

        btnSaveFile.setOnAction(event -> {
            Script script = mainApplication.getScript();
            if (script.isOpened()) {
                mainApplication.saveFile();
            } else {
                File file = WindowManager.getInstance().saveScriptFileAs();
                if (file != null) {
                    mainApplication.saveFileAs(file);
                }
            }
        });

        btnSaveFileAs.setOnAction(event -> {
            File file = WindowManager.getInstance().saveScriptFileAs();
            if (file != null) {
                mainApplication.saveFileAs(file);
            }
        });

        btnRunScript.setOnAction(event -> mainApplication.runScript());

        btnStopScript.setOnAction(event -> mainApplication.stopScript());

    }

    // status buttons
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
    private Label lblControl;

    @FXML
    private Label lblStopShortcut;


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

    @FXML
    private ToggleButton btnInsertSelectArea;

    // Combined
    @FXML
    private ToggleButton btnInsertCombinedLog;

    // Animated

    @FXML
    private ToggleButton btnInsertAnimatedMouseAt;

    @FXML
    private ToggleButton btnInsertAnimatedMouseMoveAnd;
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
     *
     * @param properties
     */
    private void initToggles(FormattedProperties properties) {
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


        //animated
        eventsRecorder.bind(btnInsertAnimatedMouseAt, new AnimatedMouseAtRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertAnimatedMouseAt);

        eventsRecorder.bind(btnInsertAnimatedMouseMoveAnd, new AnimatedMouseMoveAndRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertAnimatedMouseMoveAnd);

        // image

        eventsRecorder.bind(btnInsertSelectImage, new ImageRecorder(eventsRecorder::puImage));
        nodes.add(btnInsertSelectImage);

        eventsRecorder.bind(btnInsertSelectArea, new AreaRecorder(eventsRecorder::putCode));
        nodes.add(btnInsertSelectArea);

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
            String hint = (String) properties.get(b.getId());
            if (hint == null) {
                Out.println("Missed hint for the '" + b.getId() + "' button");
                b.setUserData(new String[]{"", ""});
            } else {
                b.setUserData(new String[]{hint, ""});
            }
            b.setOnMouseEntered(this::showHint);
            b.setOnMouseExited(this::hideHint);
        }
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
    private void initTemplateButtons(FormattedProperties prop) {

        TemplateButtonGenerator buttonGenerator = new TemplateButtonGenerator()
                .setLineSize(120)
                .setProperties(prop)
                .addStyleClass("templateButton")
                .setOnMouseEntered(this::showHint)
                .setOnMouseExited(this::hideHint)
                .setOnAction(this::insertTemplate)
                .build();

        buttonGenerator.addButtonsForKeyboardObject(keyboardTemplateButtonContainer);

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
    private void showHint(MouseEvent event) {
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
    private void hideHint(MouseEvent event) {
        areaCodeSample.setVisible(false);
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
}
