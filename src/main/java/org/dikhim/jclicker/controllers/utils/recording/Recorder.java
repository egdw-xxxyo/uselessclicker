package org.dikhim.jclicker.controllers.utils.recording;

import javafx.beans.property.BooleanProperty;

public interface Recorder {
    void start();
    
    void stop();

    BooleanProperty activeProperty();

    BooleanProperty recordingProperty();
}
