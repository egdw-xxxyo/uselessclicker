package org.dikhim.jclicker.server.socket;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import org.dikhim.jclicker.configuration.servers.ServerConfig;
import org.dikhim.jclicker.server.Server;
import org.dikhim.jclicker.util.WebUtils;

import java.util.ArrayList;
import java.util.List;

public class SocketServer implements Server {
    private IntegerProperty port = new SimpleIntegerProperty();
    private IntegerProperty currentPort = new SimpleIntegerProperty();
    private StringProperty address = new SimpleStringProperty();
    private StringProperty currentAddress = new SimpleStringProperty();
    private BooleanProperty running = new SimpleBooleanProperty();

    private SocketServerThread serverThread;

    private ServerConfig serverConfig;

    public SocketServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;

        initialization();
        updateStatus();
    }

    private void initialization() {
        port.bindBidirectional(serverConfig.getPort().valueProperty());
        updateStatus();
    }

    private void updateStatus() {
        running.setValue(serverThread != null && serverThread.isAlive());
        address.setValue(WebUtils.getLocalIpAddress());
        currentAddress.setValue(address.getValue());

        currentPort.setValue(port.getValue());
    }


    public void start() {
        if (isRunning()) stop();
        serverThread = new SocketServerThread(getPort());
        serverThread.start();
        updateStatus();
    }

    public void stop() {
        if (serverThread == null) return;
        serverThread.interrupt();
        try {
            serverThread.join(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateStatus();
    }

    public List<String> getClientsInfo() {
        if (isRunning()) {
            return serverThread.getClientsInfo();
        } else {
            return new ArrayList<>();
        }
    }

    public ObservableList<Client> getConnectedClientsProperty() {
        return getConnectedClientsProperty();
    }

    /////////////////
    public int getPort() {
        return port.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public int getCurrentPort() {
        return currentPort.get();
    }

    public IntegerProperty currentPortProperty() {
        return currentPort;
    }

    public void setCurrentPort(int currentPort) {
        this.currentPort.set(currentPort);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getCurrentAddress() {
        return currentAddress.get();
    }

    public StringProperty currentAddressProperty() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress.set(currentAddress);
    }

    public boolean isRunning() {
        return running.get();
    }

    public BooleanProperty runningProperty() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }


}
