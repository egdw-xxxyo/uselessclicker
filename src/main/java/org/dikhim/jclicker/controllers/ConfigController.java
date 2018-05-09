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
    private Button btnHotKeys;

    @FXML
    private ScrollPane rightPane;
    
    private MainConfiguration config;
    
    @FXML
    void initialize() {
        config = ClickerMain.getApplication().getMainApplication().getConfig();

        
        
        
    }

    @FXML
    void showHotkeys(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();

            root = loader.load(getClass().getResource("/ui/config/HotkeysScene.fxml").openStream());
            rightPane.setContent(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
   
}
