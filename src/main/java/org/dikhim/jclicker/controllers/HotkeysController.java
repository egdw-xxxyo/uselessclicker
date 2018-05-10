package org.dikhim.jclicker.controllers;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.hotkeys.HotKeys;
import org.dikhim.jclicker.configuration.hotkeys.Shortcut;
import org.dikhim.jclicker.configuration.values.StringValue;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private MainConfiguration config = Clicker.getApplication().getMainApplication().getConfig();


    @FXML
    void initialize() {
        assert runScriptTxt != null : "fx:id=\"runScriptTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";
        assert stopScriptTxt != null : "fx:id=\"stopScriptTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";
        assert mouseControlTxt != null : "fx:id=\"mouseControlTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";
        assert combinedControlTxt != null : "fx:id=\"combinedControlTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";

        HotKeys hotKeys = config.getHotKeys();
        List<Shortcut> shortcuts = hotKeys.getShortcutList();

        Map<String, StringValue> shortcutMap = new HashMap<>();
        
        for (Shortcut s : hotKeys.getShortcutList()) {
            shortcutMap.put(s.getName(), s.getKeys());
        }
        
        
        bindTextFields(shortcutMap);
    }


    private void bindTextFields(Map<String, StringValue> shortcutMap) {
        Bindings.bindBidirectional(runScriptTxt.textProperty(), shortcutMap.get("main/hotkeys/runScript").valueProperty());
        Bindings.bindBidirectional(stopScriptTxt.textProperty(), shortcutMap.get("main/hotkeys/stopScript").valueProperty());
        Bindings.bindBidirectional(mouseControlTxt.textProperty(), shortcutMap.get("main/hotkeys/mouseControl").valueProperty());
        Bindings.bindBidirectional(combinedControlTxt.textProperty(), shortcutMap.get("main/hotkeys/combinedControl").valueProperty());
    }
}
