package org.dikhim.jclicker.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.dikhim.jclicker.server.Server;
import org.dikhim.jclicker.server.sockets.SocketServer;

import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerSceneController {
    private SocketServer server = SocketServer.getInstance();
    @FXML
    private Button btnStartServer;

    @FXML
    private Button btnStopServer;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtStatus;

    @FXML
    private TextField txtHelp;

    @FXML
    private void initialize(){
        String status = server.getStatus();
        txtPort.setText(String.valueOf(server.getPort()));
        txtStatus.setText(status);
        txtHelp.setText("");
    }

    @FXML
    void startServer(ActionEvent event) {
        server.setPort(Integer.parseInt(txtPort.getText()));
        server.start();
        refresh();
    }

    @FXML
    void stopServer(ActionEvent event) {
        server.stop();
        refresh();
    }

    public void refresh(){
        try {
            Thread.currentThread().sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String status = server.getStatus();
        txtStatus.setText(status);
    }

}
