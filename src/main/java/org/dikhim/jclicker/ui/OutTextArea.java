package org.dikhim.jclicker.ui;

import com.sun.javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;

public class OutTextArea extends TextArea {

    public OutTextArea() {
        super();
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);

        addChangeListener(() -> appendText(""));

        TextAreaSkin customContextSkin = new TextAreaSkin(this) {
            @Override
            public void populateContextMenu(ContextMenu contextMenu) {
                super.populateContextMenu(contextMenu);

                MenuItem insert = new MenuItem("Clear");
                insert.setOnAction(event -> OutTextArea.this.clear());

                contextMenu.getItems().add(0, new SeparatorMenuItem());
                contextMenu.getItems().add(0, insert);
            }
        };
        this.setSkin(customContextSkin);
    }

    public void addChangeListener(Runnable runnable) {
        textProperty().addListener((observable, oldValue, newValue) -> runnable.run());
    }
}
