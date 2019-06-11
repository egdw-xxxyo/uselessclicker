package org.dikhim.jclicker.controllers.utils.recording;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.collections4.map.MultiValueMap;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.WindowManager;
import org.dikhim.jclicker.eventmanager.EventManager;
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
    private CodeTextArea outputTextArea;

    private BooleanProperty recorderSelected = new SimpleBooleanProperty(false);

    private BooleanProperty activeRecorder = new SimpleBooleanProperty(false);
    private BooleanProperty recording = new SimpleBooleanProperty(false);
    private BooleanProperty mouseRecording = new SimpleBooleanProperty(false);
    private BooleanProperty keyboardRecording = new SimpleBooleanProperty(false);
    private BooleanProperty lupeIsNeeded = new SimpleBooleanProperty(false);

    private ToggleGroup recordingToggleGroup = new ToggleGroup();


    private Consumer<BufferedImage> onSetOutputImage;

    private EventManager eventManager = Dependency.getEventManager();

    private MultiValueMap<Object, Runnable> toggleButtonMam = new MultiValueMap<>();

    private Map<String, Recorder> recoders = new HashMap<>();

    public EventsRecorder() {
    }

    // miscellaneous

    public void filePath() {
        File file = WindowManager.getInstance().openFile();
        if (file != null) {
            putCode(file.getAbsolutePath());
        }
    }

    //////////

    // private 
    public void putCode(String code) {
        if (outputTextArea == null) return;
        Platform.runLater(() -> outputTextArea.insertTextIntoCaretPosition(code));
    }

    public void puImage(BufferedImage bufferedImage) {
        onSetOutputImage.accept(bufferedImage);
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
        activeRecorder.set(true);
        keyboardRecording.setValue(true);
    }

    public void stopKeyboardRecording() {
        activeRecorder.set(false);
        keyboardRecording.setValue(false);
    }


    private void startMouseRecording() {
        activeRecorder.set(true);
        mouseRecording.setValue(true);
    }

    private void stopMouseRecording() {
        activeRecorder.set(false);
        mouseRecording.setValue(false);
    }

    private void showLupe() {

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

    public void bind(Button button, Recorder recorder) {
        button.setOnAction(event -> recorder.start());
    }

    private StatusHolder recordingStatus = new StatusHolder();
    
    public void bind(ToggleButton toggleButton, Recorder recorder) {
        toggleButton.setToggleGroup(recordingToggleGroup);
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                activeRecorderToggle.setUserData(toggleButton);
                activeRecorderToggle.setSelected(true);

                recordingStatus.setActiveRecorder(recorder);
                recorder.start();
            } else {
                activeRecorderToggle.setSelected(false);
                
                recorder.stop();
                recordingStatus.removeActiveRecorder();
            }
        });
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
    
    private ToggleButton activeRecorderToggle;
    
    public void addActiveRecorderToggleButton(ToggleButton toggleButton) {
        activeRecorderToggle =toggleButton;
        toggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            ToggleButton recordingtoggle = (ToggleButton) toggleButton.getUserData();
            if((recordingtoggle) == null) {
                toggleButton.setSelected(false);
                return;
            }
            toggleButton.setText(getToggleButtonPath(recordingtoggle));
            recordingtoggle.setSelected(toggleButton.isSelected());
        });
    }

    public StatusHolder getRecordingStatus() {
        return recordingStatus;
    }

    public boolean isRecorderSelected() {
        return recorderSelected.get();
    }

    public BooleanProperty recorderSelectedProperty() {
        return recorderSelected;
    }

    public boolean isActiveRecorder() {
        return activeRecorder.get();
    }

    public BooleanProperty activeRecorderProperty() {
        return activeRecorder;
    }

    public boolean isRecording() {
        return recording.get();
    }

    public BooleanProperty recordingProperty() {
        return recording;
    }
}