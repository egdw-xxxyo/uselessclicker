package org.dikhim.jclicker.server.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HelpHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Server is running\n\n" +
                "Available methods:\n" +
                "/help\n" +
                "/mouse/move  params: dx, dy\n" +
                "/mouse/press  params: button\n" +
                "/mouse/release  params: button\n" +
                "/mouse/wheel  params: direction, amount\n";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}