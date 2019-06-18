package org.dikhim.jclicker.controllers.utils.recording;

import javafx.beans.property.*;
import org.dikhim.jclicker.Dependency;

public class StatusHolder {

    private BooleanProperty recording = new SimpleBooleanProperty(false);
    private BooleanProperty active = new SimpleBooleanProperty(false);
    private Property<Recorder> activeRecorder = new SimpleObjectProperty<>();
    private BooleanProperty activeMouseRecording = new SimpleBooleanProperty(false);
    private BooleanProperty activeKeyboardRecording = new SimpleBooleanProperty(false);
    private BooleanProperty lupeIsNeeded = new SimpleBooleanProperty(false);

    private StringProperty mouseControlKey = Dependency.getConfig().hotKeys().mouseControl().keysProperty();
    private StringProperty combinedControlKey = Dependency.getConfig().hotKeys().combinedControl().keysProperty();
    private StringProperty controlKeyRequired = new SimpleStringProperty("");

    public StatusHolder() {
        activeRecorder.addListener((observable, oldValue, recorder) -> {
            if (recorder != null) {
                active.set(true);
                recording.bindBidirectional(recorder.recordingProperty());

                if (recorder instanceof KeyRecorder) {
                    activeKeyboardRecording.set(true);
                }
                if (recorder instanceof MouseRecorder) {
                    activeMouseRecording.set(true);
                }
                if (recorder instanceof LupeRequired) {
                    lupeIsNeeded.set(true);
                }
                if (recorder instanceof MouseControlKeyRequired) {
                    controlKeyRequired.set(mouseControlKey.getValue());
                } else if (recorder instanceof CombinedControlKeyRequiered) {
                    controlKeyRequired.set(combinedControlKey.getValue());

                } else if (recorder instanceof AnyKeyControlRequired) {
                    controlKeyRequired.set("ANY");
                } else {
                    controlKeyRequired.set("");
                }
            } else {
                active.set(false);
                activeMouseRecording.set(false);
                activeKeyboardRecording.set(false);
                lupeIsNeeded.set(false);
                recording.unbind();
            }
        });
    }

    public void setActiveRecorder(Recorder recorder) {
        activeRecorder.setValue(recorder);
    }

    public void removeActiveRecorder() {
        activeRecorder.setValue(null);
    }

    // getters
    public boolean isRecording() {
        return recording.get();
    }

    public BooleanProperty recordingProperty() {
        return recording;
    }

    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty activeProperty() {
        return active;
    }

    public boolean isActiveMouseRecording() {
        return activeMouseRecording.get();
    }

    public BooleanProperty activeMouseRecordingProperty() {
        return activeMouseRecording;
    }

    public boolean isActiveKeyboardRecording() {
        return activeKeyboardRecording.get();
    }

    public BooleanProperty activeKeyboardRecordingProperty() {
        return activeKeyboardRecording;
    }

    public boolean isLupeIsNeeded() {
        return lupeIsNeeded.get();
    }

    public BooleanProperty lupeIsNeededProperty() {
        return lupeIsNeeded;
    }

    public Recorder getActiveRecorder() {
        return activeRecorder.getValue();
    }

    public Property<Recorder> activeRecorderProperty() {
        return activeRecorder;
    }

    public String getControlKeyRequired() {
        return controlKeyRequired.get();
    }

    public StringProperty controlKeyRequiredProperty() {
        return controlKeyRequired;
    }
}
