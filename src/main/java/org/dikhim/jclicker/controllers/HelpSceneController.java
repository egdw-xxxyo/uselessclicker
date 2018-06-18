package org.dikhim.jclicker.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpSceneController implements Initializable {
    private ResourceBundle resourceBundle;
    
    @FXML
    private WebView web;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        WebEngine webEngine = web.getEngine();
        try {
            webEngine.load(String.valueOf(getClass().getResource(resourceBundle.getString("index")).toURI().toURL()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
