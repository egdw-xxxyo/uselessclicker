package org.dikhim.jclicker.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.WindowManager;
import org.dikhim.jclicker.configuration.hotkeys.HotKeys;

import java.net.URL;
import java.util.ResourceBundle;

public class HotkeysController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField runScriptTxt;

    @FXML
    private TextField stopScriptTxt;

    @FXML
    private TextField mouseControlTxt;

    @FXML
    private TextField combinedControlTxt;

    @FXML
    void initialize() {
        HotKeys hotKeys = Dependency.getConfig().hotKeys();
        Bindings.bindBidirectional(runScriptTxt.textProperty(), hotKeys.runScript().keysProperty());
        Bindings.bindBidirectional(stopScriptTxt.textProperty(), hotKeys.stopScript().keysProperty());
        Bindings.bindBidirectional(mouseControlTxt.textProperty(), hotKeys.mouseControl().keysProperty());
        Bindings.bindBidirectional(combinedControlTxt.textProperty(), hotKeys.combinedControl().keysProperty());
    }

    @FXML
    void smRunScriptEdit() {
        String keys = WindowManager.showShortcutRecordingDialog("settings");
        if(!keys.isEmpty()) runScriptTxt.setText(keys);
    }

    @FXML
    void smStopScriptEdit() {
        String keys = WindowManager.showShortcutRecordingDialog("settings");
        if(!keys.isEmpty()) stopScriptTxt.setText(keys);
    }

    @FXML
    void mrControlKeyEdit() {
        String keys = WindowManager.showShortcutRecordingDialog("settings");
        if(keys != null && !keys.isEmpty()) mouseControlTxt.setText(keys);
    }

    @FXML
    void crControlKeyEdit() {
        String keys = WindowManager.showShortcutRecordingDialog("settings");
        if(!keys.isEmpty()) combinedControlTxt.setText(keys);
    }
}
