package org.dikhim.jclicker.server.sockets;

import javafx.collections.ObservableList;
import org.dikhim.jclicker.server.Client;
import org.dikhim.jclicker.server.Server;
import org.dikhim.jclicker.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Server {
    private int portNumber = 5000;
    private SocketServerThread serverThread;

    private static SocketServer socketServer;

    private SocketServer() {
    }

    public static SocketServer getInstance() {
        if (socketServer == null) socketServer = new SocketServer();
        return socketServer;
    }

    public String getAddress() {
        return WebUtils.getLocalIpAddress();
    }

    public int getPort() {
        return portNumber;
    }

    public void setPort(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getStatus() {
        return String.format("active: %b, address: %s:%d", isActive(), getAddress(), getPort());
    }

    public boolean isActive() {
        return serverThread != null && serverThread.isAlive();
    }

    public void start() {
        if (isActive()) stop();
        serverThread = new SocketServerThread(portNumber);
        serverThread.start();
    }

    public void restart() {
        stop();
        start();
    }

    public void stop() {
        if (serverThread == null) return;
        serverThread.interrupt();
        try {
            serverThread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<String> getClientsInfo() {
        if(isActive()){
            return serverThread.getClientsInfo();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public ObservableList<Client> getConnectedClients() {
        return socketServer.getConnectedClients();
    }
}
