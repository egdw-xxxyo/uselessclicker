package org.dikhim.jclicker.ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChooseLanguageDialogController implements Initializable {
    private Stage dialogStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    private String selectedLanguage = "en";
    @FXML
    void english(){
        selectedLanguage = "en";
        dialogStage.close();
    }
    
    @FXML
    void russian() {
        selectedLanguage = "ru";
        dialogStage.close();

    }
    
    public String getSelectedLanguage() {
        return selectedLanguage;
    }
    
    

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
