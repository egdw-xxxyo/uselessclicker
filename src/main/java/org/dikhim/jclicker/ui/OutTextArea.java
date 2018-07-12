package org.dikhim.jclicker.ui;

import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class OutTextArea extends TextArea {

    public OutTextArea() {
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        
        textProperty().addListener((observable, oldValue, newValue) -> appendText(""));
    }
}
