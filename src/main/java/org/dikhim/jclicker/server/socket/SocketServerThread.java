package org.dikhim.jclicker.server.socket;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.dikhim.jclicker.util.WebUtils;
import org.dikhim.jclicker.util.output.Out;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.List;

public class SocketServerThread extends Thread {
    private int port;

    private ObservableList<ClientSocketThread> connectedClients = FXCollections.observableArrayList();

    public SocketServerThread(int port) {
        super("SocketServerThread");
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)
        ) {
            Out.println("SocketServer started on " + WebUtils.getLocalIpAddress() + ":" + port);
            // set timeout for waiting client
            serverSocket.setSoTimeout(500);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();

                    ClientSocketThread clientThread = new ClientSocketThread(clientSocket,this);
                    clientThread.start();
                } catch (SocketTimeoutException timeOutE) {
                    // if server is interrupted
                    if (isInterrupted()) {
                        interruptClientsThreads();
                        break;
                    }
                }
            }
            Out.println("Server stopped");
        } catch (IOException | IllegalArgumentException e) {
            Out.println(e.getMessage());
        }
    }

    public List<String> getClientsInfo() {
        List<String> clients = new ArrayList<>();
        for (Thread t : connectedClients) {
            clients.add(t.getName());
        }
        return clients;
    }

    private synchronized void interruptClientsThreads() {
        for (Thread thread : connectedClients) {
            thread.interrupt();
        }
    }

    void addClient(ClientSocketThread clientSocketThread) {
        Platform.runLater(() -> {
            connectedClients.add(clientSocketThread);
            Out.println("Connected: " + clientSocketThread.getName());
        });
    }

    void removeClient(ClientSocketThread clientSocketThread){
        Platform.runLater(() -> {
            connectedClients.remove(clientSocketThread);
            Out.println("Disconnected: " + clientSocketThread.getName());
        });
    }


    public ObservableList<ClientSocketThread> getConnectedClients() {
        return connectedClients;
    }
}
