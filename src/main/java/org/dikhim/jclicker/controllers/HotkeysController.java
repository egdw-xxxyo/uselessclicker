package org.dikhim.jclicker.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.configuration.newconfig.hotkeys.HotKeys;

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
        HotKeys hotKeys = Dependency.getConfiguration().hotKeys();
        Bindings.bindBidirectional(runScriptTxt.textProperty(), hotKeys.runScript().keysProperty());
        Bindings.bindBidirectional(stopScriptTxt.textProperty(), hotKeys.stopScript().keysProperty());
        Bindings.bindBidirectional(mouseControlTxt.textProperty(), hotKeys.mouseControl().keysProperty());
        Bindings.bindBidirectional(combinedControlTxt.textProperty(), hotKeys.combinedControl().keysProperty());
    }
}
