package org.dikhim.jclicker.jfxcomponents.statuspane;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import org.dikhim.jclicker.jfxcomponents.CustomFxmlControl;

public class StatusPane extends CustomFxmlControl {

    @FXML
    private ToggleButton btnScriptStatus;

    public StatusPane() {
        super("/StatusPane.fxml");
    }
    
    @FXML
    public void onBtnStatusScript() {
    }
}
