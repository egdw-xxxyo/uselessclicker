package org.dikhim.jclicker.server.http;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.dikhim.jclicker.configuration.servers.ServerConfig;
import org.dikhim.jclicker.jsengine.objects.ComputerObject;
import org.dikhim.jclicker.jsengine.objects.JsKeyboardObject;
import org.dikhim.jclicker.jsengine.objects.JsMouseObject;
import org.dikhim.jclicker.jsengine.objects.JsSystemObject;
import org.dikhim.jclicker.server.http.handler.*;
import org.dikhim.jclicker.util.WebUtils;

import java.awt.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class HttpServer {
    private com.sun.net.httpserver.HttpServer httpHttpServer;
    private IntegerProperty port = new SimpleIntegerProperty();

    private ComputerObject defaultComputerObject;

    private List<HttpClient> clients = new ArrayList<>();
    private HttpClient defaultClient;

    ServerConfig serverConfig;
    
    public HttpServer(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        try {
            Robot robot = new Robot();
            JsKeyboardObject keyboardObject = new JsKeyboardObject(robot);
            JsMouseObject mouseObject = new JsMouseObject(robot);
            JsSystemObject systemObject = new JsSystemObject(robot);
            defaultComputerObject = new ComputerObject(keyboardObject, mouseObject, systemObject);
            defaultClient = new HttpClient(0, defaultComputerObject);
            bindConfig();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void bindConfig() {
        port.bindBidirectional(serverConfig.getPort().valueProperty());
    }
    
    public void start() {
        try {
            if (httpHttpServer != null) httpHttpServer.stop(0);
            httpHttpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(getPort()), 0);
            createContext();
            httpHttpServer.setExecutor(null);
            httpHttpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void stop() {
        if (httpHttpServer != null) {
            httpHttpServer.stop(0);
            httpHttpServer = null;
        }
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
            if(h.getUid() == uid) return h;
        }

        return defaultClient;
    }


    public int getPort() {
        if (httpHttpServer == null) {
            return port.get();
        } else {
            return httpHttpServer.getAddress().getPort();
        }
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public IntegerProperty portProperty() {
        return port;
    }
}
