package org.dikhim.jclicker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.configuration.newconfig.localization.Language;
import org.dikhim.jclicker.configuration.newconfig.localization.Localization;

import java.util.List;
import java.util.ResourceBundle;

public class LanguageController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private ChoiceBox<Language> languageChoiceBox;

    @FXML
    private TextField runScriptTxt;

    @FXML
    void initialize() {
        Localization localization = Dependency.getConfiguration().localization();

        List<Language> list = localization.languages().list();
        languageChoiceBox.setItems(FXCollections.observableArrayList(list));
        languageChoiceBox.getSelectionModel().select(
                list.stream()
                        .filter(language -> language.id().equals(localization.getApplicationLanguageId()))
                        .findFirst()
                        .get());
        Bindings.bindBidirectional(localization.applicationLanguageIdProperty(), languageChoiceBox.valueProperty(), new StringConverter<Language>() {
            @Override
            public String toString(Language object) {
                return object.id();
            }

            @Override
            public Language fromString(String string) {
                return localization
                        .languages()
                        .list()
                        .stream()
                        .filter(language -> language.id().equals(string))
                        .findFirst()
                        .get();

            }
        });
    }
}
