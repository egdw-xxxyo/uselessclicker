package org.dikhim.jclicker.jfxcomponents.statuspane;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import org.dikhim.jclicker.jfxcomponents.CustomFxmlControl;

public class StatusPane extends CustomFxmlControl {

    @FXML
    private ToggleButton btnScript;

    @FXML
    private ToggleButton btnActiveRecorder;

    @FXML
    private ToggleButton btnLupe;

    @FXML
    private ToggleButton btnMouse;

    @FXML
    private ToggleButton btnKeyboard;

    @FXML
    private ToggleButton btnRecording;

    @FXML
    private Label lblControl;

    private StringProperty mouseControlProperty = new SimpleStringProperty();

    private StringProperty combinedControlProperty = new SimpleStringProperty();

    private StringProperty controlKeyRequired = new SimpleStringProperty();


    public StatusPane() {
        super("/StatusPane.fxml");

        controlKeyRequired.addListener((observable, oldValue, newValue) -> {
            switch (newValue.toUpperCase()) {
                case "MOUSE":
                    lblControl.setText(mouseControlProperty.getValue());
                    break;
                case "COMBINED":
                    lblControl.setText(combinedControlProperty.getValue());
                    break;

                case "KEYBOARD":
                    lblControl.setText("ANY");
                    break;
                case "NONE":
                    lblControl.setText("");
                    break;
            }
        });
    }


    public void bindControlRequiredProperty(StringProperty stringProperty) {
        controlKeyRequired.bind(stringProperty);
    }

}
