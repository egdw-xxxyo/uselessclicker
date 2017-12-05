package org.dikhim.jclicker.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.dikhim.jclicker.jsengine.KeyboardObject;
import org.dikhim.jclicker.jsengine.MouseObject;
import org.dikhim.jclicker.jsengine.SystemObject;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private MouseObject mouse;
    private KeyboardObject keyboard;
    private SystemObject system;
    private HttpServer httpServer;

    private int port = 5000;

    private static Server server;

    private Server() {
        try {

            Robot robot = new Robot();
            mouse = new MouseObject(robot);
            keyboard = new KeyboardObject(robot);
            system = new SystemObject(robot);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static Server getInstance() {
        if (server == null) server = new Server();
        return server;
    }

    public void start() {
        try {
            if (httpServer != null) httpServer.stop(0);
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            createContext();
            httpServer.setExecutor(null);
            httpServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void stop() {
        httpServer.stop(0);
        httpServer = null;
    }

    public int getPort() {
        if (httpServer == null) {
            return port;
        } else {
            return httpServer.getAddress().getPort();
        }
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual())
                    continue;


                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        ip = addr.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }

    public String getStatus() {
        if (httpServer == null) {
            return "Stopped";
        } else {
            return "Running on " + getAddress() + ":" + getPort();
        }
    }

    public boolean isAvailable(){
        return httpServer!=null;
    }

    ////static

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }


    private void createContext(){
        httpServer.createContext("/help",new HelpHandler());
        httpServer.createContext("/mouse/move", new MouseMoveHandler());

    }
    static class HelpHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            StringBuilder sb = new StringBuilder("");
            sb.append("Server is running\n\n");
            sb.append("Available methods:\n");
            sb.append("/help\n");
            sb.append("/mouse/move  params: dx, dy\n");

            String response = sb.toString();
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
    static class MouseMoveHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "Okay";
            String query = httpExchange.getRequestURI().getQuery();
            if (query == null) return;
            Map<String, String> params = queryToMap(query);
            int dx = Integer.parseInt(params.get("dx"));
            int dy = Integer.parseInt(params.get("dy"));

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            server.mouse.move(dx, dy);
        }
    }
}
