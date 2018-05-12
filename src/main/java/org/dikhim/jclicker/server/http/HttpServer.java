package org.dikhim.jclicker.server.http;


import org.dikhim.jclicker.jsengine.objects.ComputerObject;
import org.dikhim.jclicker.server.http.handler.*;
import org.dikhim.jclicker.util.WebUtils;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServer {
    private com.sun.net.httpserver.HttpServer httpHttpServer;
    private int port = 5000;

    private ComputerObject computerObject;

    public HttpServer(ComputerObject computerObject) {
        this.computerObject = computerObject;
    }

    public void start() {
        try {
            if (httpHttpServer != null) httpHttpServer.stop(0);
            httpHttpServer = com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
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

    public int getPort() {
        if (httpHttpServer == null) {
            return port;
        } else {
            return httpHttpServer.getAddress().getPort();
        }
    }

    public void setPort(int port) {
        this.port = port;
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
        httpHttpServer.createContext("/help", new HelpHttpHandler());
        httpHttpServer.createContext("/mouse/move", new MouseMoveHttpHandler(computerObject));
        httpHttpServer.createContext("/mouse/press", new MousePressHttpHandler(computerObject));
        httpHttpServer.createContext("/mouse/release", new MouseReleaseHttpHandler(computerObject));
        httpHttpServer.createContext("/mouse/wheel", new MouseWheelHttpHandler(computerObject));
    }

}
