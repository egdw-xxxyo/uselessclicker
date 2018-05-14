package org.dikhim.jclicker.server.socket;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import org.dikhim.jclicker.configuration.servers.ServerConfig;
import org.dikhim.jclicker.server.Server;
import org.dikhim.jclicker.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Server {
    private IntegerProperty port = new SimpleIntegerProperty();
    private SocketServerThread serverThread;

    private ServerConfig serverConfig;

    public SocketServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        bindConfig();
    }

    @Override
    public String getAddress() {
        return WebUtils.getLocalIpAddress();
    }

    @Override
    public int getPort() {
        return port.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public void setPort(int port) {
        this.port.set(port);
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
        serverThread = new SocketServerThread(getPort());
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
        return getConnectedClientsProperty();
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
    
    private void bindConfig(){
        port.bindBidirectional(serverConfig.getPort().valueProperty());
    }
}
