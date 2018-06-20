package org.dikhim.jclicker.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.controllers.utils.WebViewObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelpSceneController implements Initializable {
    private ResourceBundle resourceBundle;
    private WebViewObject webViewObject;
    @FXML
    private WebView web;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webViewObject = new WebViewObject(Clicker.getApplication()::openInBrowser);

        this.resourceBundle = resources;
        WebEngine webEngine = web.getEngine();
        try {
            webEngine.load(String.valueOf(getClass().getResource(resourceBundle.getString("index")).toURI().toURL()));

            webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    window.setMember("sys", webViewObject);
                }
            }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
