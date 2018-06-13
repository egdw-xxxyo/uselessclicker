package org.dikhim.jclicker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.actions.utils.encoders.ActionEncoderFactory;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.configuration.hotkeys.HotKeys;
import org.dikhim.jclicker.configuration.hotkeys.Shortcut;
import org.dikhim.jclicker.configuration.localization.Language;
import org.dikhim.jclicker.configuration.localization.Localization;
import org.dikhim.jclicker.configuration.values.StringValue;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LanguageController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private ChoiceBox<Language> selectedLanguage;


    @FXML
    private TextField runScriptTxt;

    private MainConfiguration config = Clicker.getApplication().getMainApplication().getConfig();


    @FXML
    void initialize() {
        assert runScriptTxt != null : "fx:id=\"runScriptTxt\" was not injected: check your FXML file 'HotkeysScene.fxml'.";

        Localization localization = config.getLocalization();
        List<String> listOfNames = localization
                .getLanguages()
                .getLanguageList()
                .stream()
                .map(Language::getNativeName)
                .map(StringValue::get)
                .collect(Collectors.toList());
        
        selectedLanguage.setItems(FXCollections.observableArrayList(localization.getLanguages().getLanguageList()));
        selectedLanguage.getSelectionModel().select(localization.getSelectedLanguage());
        Bindings.bindBidirectional(localization.getApplicationLanguage().valueProperty(), selectedLanguage.valueProperty(), new StringConverter<Language>() {
            @Override
            public String toString(Language object) {
                return object.getId().get();
            }

            @Override
            public Language fromString(String string) {
                localization.getLanguages().getById(string);
                return null;
            }
        });


        System.out.println(localization.getApplicationLanguage().get());
    }
}
