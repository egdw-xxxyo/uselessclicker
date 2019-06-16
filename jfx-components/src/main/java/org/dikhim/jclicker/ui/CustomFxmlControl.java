package org.dikhim.jclicker.ui;

import javafx.scene.control.Skin;

public class CustomFxmlControl extends CustomControl {
    
    private String fxml;
    protected CustomFxmlControl(String fxml) {
        this.fxml = fxml;
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FxmlSkin<>(this, fxml);
    }
}
