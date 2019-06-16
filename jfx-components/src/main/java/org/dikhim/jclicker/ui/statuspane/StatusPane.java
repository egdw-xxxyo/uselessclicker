package org.dikhim.jclicker.ui.statuspane;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import org.dikhim.jclicker.ui.CustomFxmlControl;

public class StatusPane extends CustomFxmlControl {

    @FXML
    private ToggleButton btnScriptStatus;

    public StatusPane() {
        super("/ui/statuspane/StatusPane.fxml");
    }
    
    @FXML
    public void scriptStatus() {
    }
}
