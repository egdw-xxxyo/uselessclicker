package org.dikhim.jclicker.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.dikhim.jclicker.jsengine.KeyboardObject;
import org.dikhim.jclicker.jsengine.MouseObject;
import org.dikhim.jclicker.jsengine.SystemObject;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class Server {
    private MouseObject mouse;
    private KeyboardObject keyboard;
    private SystemObject system;
    private HttpServer httpServer;
    private int port = 5000;
    private List<HttpContext> listOfContexts = new ArrayList<>();

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

        mouse.setAllDelays(0);
        keyboard.setAllDelays(0);
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
        if(httpServer!=null){
            httpServer.stop(0);
            httpServer = null;
        }
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

    public boolean isAvailable() {
        return httpServer != null;
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


    // Contexts
    private void createContext() {
        if (listOfContexts.isEmpty()) {
            listOfContexts.add(new HttpContext("/help", new HelpHandler()));
            listOfContexts.add(new HttpContext("/mouse/move", new MouseMoveHandler()));
            listOfContexts.add(new HttpContext("/mouse/press",new MousePress()));
            listOfContexts.add(new HttpContext("/mouse/release",new MouseRelease()));
            listOfContexts.add(new HttpContext("/mouse/wheel",new MouseWheel()));
        }

        for(HttpContext context:listOfContexts){
            httpServer.createContext(context.getPath(),context.getHandler());
        }
    }

    // /help
    static class HelpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            StringBuilder sb = new StringBuilder("");
            sb.append("Server is running\n\n");
            sb.append("Available methods:\n");
            sb.append("/help\n");
            sb.append("/mouse/move  params: dx, dy\n");

            sb.append("/mouse/press  params: button\n");
            sb.append("/mouse/release  params: button\n");
            sb.append("/mouse/wheel  params: direction, amount\n");

            String response = sb.toString();
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // /mouse/move
    static class MouseMoveHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            if (query == null) return;
            Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            int dx = Integer.parseInt(params.get("dx"));
            int dy = Integer.parseInt(params.get("dy"));
            server.mouse.move(dx, dy);

            String response = "Moved dx="+dx+" dy="+dy;
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // /mouse/press
    static class MousePress implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            if (query == null) return;
            Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            String button = params.get("button");
            server.mouse.press(button);

            String response = "Pressed button="+button;
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // /mouse/release
    static class MouseRelease implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            if (query == null) return;
            Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            String button = params.get("button");
            server.mouse.release(button);

            String response = "Released button="+button;
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }

    }

    // /mouse/wheel
    static class MouseWheel implements HttpHandler{

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            if (query == null) return;
            Map<String, String> params = queryToMap(httpExchange.getRequestURI().getQuery());
            String direction =  params.get("direction");
            int amount = Integer.parseInt(params.get("amount"));
            server.mouse.wheel(direction,amount);

            String response = "Wheeled direction="+direction+" amount="+amount;
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
