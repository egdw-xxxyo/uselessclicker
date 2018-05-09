package org.dikhim.jclicker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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
        assert runScriptTxt != null : "fx:id=\"runScriptTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";
        assert stopScriptTxt != null : "fx:id=\"stopScriptTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";
        assert mouseControlTxt != null : "fx:id=\"mouseControlTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";
        assert combinedControlTxt != null : "fx:id=\"combinedControlTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";

    }

    
}
