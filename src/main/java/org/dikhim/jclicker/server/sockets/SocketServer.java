package org.dikhim.jclicker.server.sockets;

import org.dikhim.jclicker.server.AbstractServer;
import org.dikhim.jclicker.util.WebUtils;
import org.dikhim.jclicker.util.output.Out;

public class SocketServer implements AbstractServer {
    private int portNumber = 5000;
    private Thread serverThread;

    private static SocketServer socketServer;

    private SocketServer() {
    }

    public static SocketServer getInstance() {
        if (socketServer == null) socketServer = new SocketServer();
        return socketServer;
    }


    @Override
    public String getAddress() {
        return WebUtils.getLocalIpAddress();
    }

    @Override
    public int getPort() {
        return portNumber;
    }

    @Override
    public void setPort(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public String getStatus() {
        return String.format("active: %b, address: %s:%d", isActive(), getAddress(), getPort());
    }

    @Override
    public boolean isActive() {
        if (serverThread == null) return false;
        return serverThread.isAlive();
    }

    @Override
    public void start() {
        if (isActive()) stop();
        serverThread = new SocketServerThread(portNumber);
        serverThread.start();
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public void stop() {
        if(serverThread==null)return;
        serverThread.interrupt();
        try {
            serverThread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
