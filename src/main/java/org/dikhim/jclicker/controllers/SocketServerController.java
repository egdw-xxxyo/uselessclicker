package org.dikhim.jclicker.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.server.socket.SocketServer;
import org.dikhim.jclicker.util.Converters;

public class SocketServerController {
    private SocketServer server;
    private MainApplication mainApplication = Clicker.getApplication().getMainApplication();
    @FXML
    private Button btnStartServer;

    @FXML
    private Button btnStopServer;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtStatus;

    @FXML
    private ListView<String> listClients;
    ObservableList<String> clientList = FXCollections.observableArrayList();


    @FXML
    private void initialize() {
        server = mainApplication.getSocketServer();
        listClients.setItems(clientList);

        bindConfig();
    }

    @FXML
    void startServer(ActionEvent event) {
        server.setPort(Integer.parseInt(txtPort.getText()));
        server.start();
    }

    @FXML
    void stopServer(ActionEvent event) {
        server.stop();
    }

    private void bindConfig() {
        Bindings.bindBidirectional(txtPort.textProperty(), server.portProperty(), Converters.getStringToNumberConvertor());
        txtStatus.textProperty().bind(Bindings.
                concat(
                        server.currentAddressProperty(),
                        ":", server.currentPortProperty(),
                        " is running:", server.runningProperty()));
    }
}
