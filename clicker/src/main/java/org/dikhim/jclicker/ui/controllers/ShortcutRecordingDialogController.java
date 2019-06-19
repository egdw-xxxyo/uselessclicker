package org.dikhim.jclicker.ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.listener.SimpleKeyboardPressListener;

import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ShortcutRecordingDialogController implements Initializable {
    private volatile Stage dialogStage;
    private final Set<String> keys = new LinkedHashSet<>();

    @FXML
    private volatile TextField txt;
    private volatile String shortcutToReturn = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Dependency.getEventManager().addListener(new SimpleKeyboardPressListener("dialog.shortcut.recording") {
            @Override
            public void keyPressed(KeyPressEvent event) {
                if (isFocused()) {
                    keys.add(event.getKey());
                    update();
                }
            }
        });
        txt.requestFocus();
    }

    @FXML
    void clear() {
        keys.clear();
        txt.clear();
    }

    @FXML
    void cancel() {
        dialogStage.close();
    }

    @FXML
    void ok() {
        shortcutToReturn = txt.getText();
        dialogStage.close();
    }

    private boolean isFocused() {
        return txt.isFocused();
    }

    private void update() {
        Platform.runLater(() -> txt.setText(String.join(" ", keys)));
    }

    public String getShortcut() {
        return shortcutToReturn;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        dialogStage.setOnHiding(event -> {
            Dependency.getEventManager().removeListener("dialog.shortcut.recording");
        });
    }
}
