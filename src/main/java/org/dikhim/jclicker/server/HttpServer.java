package org.dikhim.jclicker.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.dikhim.jclicker.jsengine.objects.JsKeyboardObject;
import org.dikhim.jclicker.jsengine.objects.JsMouseObject;
import org.dikhim.jclicker.jsengine.objects.JsSystemObject;
import org.dikhim.jclicker.util.WebUtils;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class HttpServer {
    private JsMouseObject mouse;
    private JsKeyboardObject keyboard;
    private JsSystemObject system;
    private com.sun.net.httpserver.HttpServer httpHttpServer;
    private int port = 5000;
    private List<HttpContext> listOfContexts = new ArrayList<>();

    private static HttpServer httpServer;

    private HttpServer() {
        try {

            Robot robot = new Robot();
            mouse = new JsMouseObject(robot);
            keyboard = new JsKeyboardObject(robot);
            system = new JsSystemObject(robot);


        } catch (AWTException e) {
            e.printStackTrace();
        }

        mouse.setDelays(0);
        keyboard.setDelays(0);
    }

    public static HttpServer getInstance() {
        if (httpServer == null) httpServer = new HttpServer();
        return httpServer;
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
        if(httpHttpServer !=null){
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
            httpHttpServer.createContext(context.getPath(),context.getHandler());
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
            httpServer.mouse.move(dx, dy);

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
            httpServer.mouse.press(button);

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
            httpServer.mouse.release(button);

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
            httpServer.mouse.wheel(direction,amount);

            String response = "Wheeled direction="+direction+" amount="+amount;
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
