package org.dikhim.jclicker.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.dikhim.jclicker.server.socket.SocketServer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerSceneController implements Initializable {
    private ResourceBundle resourceBundle;
    
    @FXML
    private AnchorPane httpPane;

    @FXML
    private AnchorPane socketPane;

    @FXML
    private void initialize(){
        
    }


    private void loadHttp(){
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/server/HttpServerScene.fxml"));
            loader.setResources(resourceBundle);
            root = loader.load();
            
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            httpPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSocket(){
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/server/SocketServerScene.fxml"));
            loader.setResources(resourceBundle);
            root = loader.load();
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            socketPane.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        loadHttp();
        loadSocket();
    }
}
