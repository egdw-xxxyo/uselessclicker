package org.dikhim.jclicker.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import org.dikhim.jclicker.server.socket.SocketServer;

import java.io.IOException;

public class ServerSceneController {
    @FXML
    private AnchorPane httpPane;

    @FXML
    private AnchorPane socketPane;

    @FXML
    private void initialize(){
        loadHttp();
        loadSocket();
    }


    private void loadHttp(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/ui/server/HttpServerScene.fxml"));

            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            httpPane.getChildren().add(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadSocket(){
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/ui/server/SocketServerScene.fxml"));
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            socketPane.getChildren().add(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
