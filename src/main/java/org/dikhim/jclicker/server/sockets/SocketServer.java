package org.dikhim.jclicker.server.sockets;

import org.dikhim.jclicker.server.AbstractServer;
import org.dikhim.jclicker.util.WebUtils;

import java.util.List;

public class SocketServer implements AbstractServer {
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
        return serverThread.getClientsInfo();
    }
}
