package org.dikhim.jclicker.server.http;


import javafx.beans.property.*;
import org.dikhim.clickauto.ClickAuto;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.configuration.servers.ServerConfig;
import org.dikhim.jclicker.jsengine.clickauto.objects.*;
import org.dikhim.jclicker.server.http.handler.*;
import org.dikhim.jclicker.util.WebUtils;
import org.dikhim.jclicker.util.Out;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class HttpServer {
    private com.sun.net.httpserver.HttpServer httpHttpServer;
    private StringProperty address = new SimpleStringProperty();
    private StringProperty currentAddress = new SimpleStringProperty();
    private IntegerProperty port = new SimpleIntegerProperty();
    private IntegerProperty currentPort = new SimpleIntegerProperty();
    private BooleanProperty running = new SimpleBooleanProperty();
    private ComputerObject defaultComputerObject;

    private List<HttpClient> clients = new ArrayList<>();
    private HttpClient defaultClient;

    ServerConfig serverConfig;
    private ClickAuto clickAuto;

    public HttpServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.clickAuto = Dependency.getClickAuto();
        
        KeyboardObject keyboardObject = new UselessKeyboardObject(clickAuto.robot());
        MouseObject mouseObject = new UselessMouseObject(clickAuto.robot());
        SystemObject systemObject = new UselessSystemObject(clickAuto.getEngine());
        defaultComputerObject = new ComputerObject(keyboardObject, mouseObject, systemObject);
        defaultClient = new HttpClient(0, defaultComputerObject);
        initialization();
    }

    private void initialization() {
        port.bindBidirectional(serverConfig.getPort().valueProperty());
        updateStatus();
    }

    private void updateStatus() {
        running.setValue(httpHttpServer != null);
        address.setValue(WebUtils.getLocalIpAddress());
        currentAddress.setValue(address.getValue());

        currentPort.setValue(port.getValue());
    }

    public void start() {
        try {
            if (httpHttpServer != null) stop();
            httpHttpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(getPort()), 0);
            createContext();
            httpHttpServer.setExecutor(null);
            httpHttpServer.start();
            updateStatus();
            Out.println("HttpServer started on " + currentAddress.get() + ":" + currentPort.get());
        } catch (IOException e) {
            e.printStackTrace();
            httpHttpServer = null;
            updateStatus();
            Out.println("HttpServer failed to start on " + currentAddress.get() + ":" + currentPort.get());

        }


    }

    public void stop() {
        if (httpHttpServer != null) {
            httpHttpServer.stop(0);
            httpHttpServer = null;
            Out.println("HttpServer has been stopped");

        }
        updateStatus();
    }


    public String getAddress() {
        return WebUtils.getLocalIpAddress();
    }

    public String getStatus() {
        if (httpHttpServer == null) {
            return "Stopped";
        } else {
            return "Running on " + getAddress() + ":" + getPort();
        }
    }

    public boolean isAvailable() {
        return httpHttpServer != null;
    }


    // Contexts
    private void createContext() {
        httpHttpServer.createContext("/help", new HelpHttpHandler(this));
        httpHttpServer.createContext("/mouse/move", new MouseMoveHttpHandler(this));
        httpHttpServer.createContext("/mouse/press", new MousePressHttpHandler(this));
        httpHttpServer.createContext("/mouse/release", new MouseReleaseHttpHandler(this));
        httpHttpServer.createContext("/mouse/wheel", new MouseWheelHttpHandler(this));
    }


    public HttpClient getClientByUid(int uid) {
        for (HttpClient h : clients) {
            if (h.getUid() == uid) return h;
        }

        return defaultClient;
    }

///////////////////////


    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public int getPort() {
        if (httpHttpServer == null) {
            return port.get();
        } else {
            return httpHttpServer.getAddress().getPort();
        }
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

    public void setPort(int port) {
        this.port.set(port);
    }

    public IntegerProperty portProperty() {
        return port;
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
