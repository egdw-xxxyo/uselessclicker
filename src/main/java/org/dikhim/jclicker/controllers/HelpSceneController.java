package org.dikhim.jclicker.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;

public class HelpSceneController {
    @FXML
    private WebView web;
    
    @FXML
    private void initialize() {
        WebEngine webEngine = web.getEngine();
        try {
            webEngine.load(String.valueOf(getClass().getResource("/docs/index.html").toURI().toURL()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
