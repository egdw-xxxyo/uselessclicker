package org.dikhim.jclicker.server.socket;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.dikhim.jclicker.server.Server;
import org.dikhim.jclicker.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Server {
    private int portNumber = 5000;
    private SocketServerThread serverThread;

    private static SocketServer socketServer;
    
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
        return serverThread != null && serverThread.isAlive();
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
    public ObservableList<Client> getConnectedClientsProperty() {
        return socketServer.getConnectedClientsProperty();
    }

    @Override
    public IntegerProperty getPortProperty() {
        //TODO
        return null;
    }

    @Override
    public StringProperty getStatusProperty() {
        //TODO
        return null;
    }

    @Override
    public StringProperty getAddressProperty() {
        //TODO
        return null;
    }
}
