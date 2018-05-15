package org.dikhim.jclicker.controllers;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class HelpSceneController {
    @FXML
    private WebView web;
    
    @FXML
    private void initialize() throws MalformedURLException {
        WebEngine webEngine = web.getEngine();
        webEngine.load("/strings/html/help.html");
        try {
            webEngine.load("http://diana-adrianne.com/purecss-francine/");
            //webEngine.load(String.valueOf(getClass().getResource("/strings/html/help.html").toURI().toURL()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
