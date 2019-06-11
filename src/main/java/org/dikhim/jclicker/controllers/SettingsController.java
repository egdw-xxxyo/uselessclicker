package org.dikhim.jclicker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.configuration.Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private ResourceBundle resourceBundle;
    
    @FXML
    private ScrollPane rightPane;

    private Configuration configuration = Dependency.getConfig();

    @FXML
    void initialize() {

    }

    @FXML
    void showHotkeys(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/config/HotkeysScene.fxml"));
            loader.setResources(resourceBundle);
            root = loader.load();

            rightPane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showLanguage(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/config/LanguageScene.fxml"));
            loader.setResources(resourceBundle);
            root = loader.load();

            rightPane.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void resetToDefault(ActionEvent event) {
        configuration.resetToDefault();
    }

    @FXML
    void discardChanges(ActionEvent event) {
        configuration.resetToSaved();
    }

    @FXML
    void save(ActionEvent event) {
        configuration.save();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
    }
}
