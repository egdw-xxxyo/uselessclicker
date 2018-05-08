package org.dikhim.jclicker.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class ConfigController {
    @FXML
    private AnchorPane leftPane;
    
    @FXML
    private AnchorPane rightPane;

    @FXML
    private TreeView<String> treeView;

    @FXML
    void initialize() {
        TreeItem<String> rootItem = new TreeItem<>("Root");
        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<String> ("Message" + i);
            rootItem.getChildren().add(item);
        }
        treeView.setRoot(rootItem);
    }
}
