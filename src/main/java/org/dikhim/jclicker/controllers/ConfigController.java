package org.dikhim.jclicker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.dikhim.jclicker.ClickerMain;
import org.dikhim.jclicker.configuration.MainConfiguration;

import java.io.IOException;

public class ConfigController {
    @FXML
    public Button resetBtn;

    @FXML
    public Button saveBtn;
    
    @FXML
    public Button hotkeysBtn;


    @FXML
    private ScrollPane rightPane;

    private MainConfiguration config = ClickerMain.getApplication().getMainApplication().getConfig();

    @FXML
    void initialize() {

    }

    @FXML
    void showHotkeys(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/ui/config/HotkeysScene.fxml"));

            rightPane.setContent(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void resetConfig(ActionEvent event) {
        config.setDefault();
    }

    @FXML
    void saveConfig(ActionEvent event) {
        config.save();
    }

}