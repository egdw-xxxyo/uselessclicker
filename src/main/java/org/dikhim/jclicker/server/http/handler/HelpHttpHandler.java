package org.dikhim.jclicker.server.http.handler;

import org.dikhim.jclicker.server.http.HttpServer;

import java.io.IOException;

public class HelpHttpHandler extends DefaultHttpHandler {

    public HelpHttpHandler(HttpServer httpServer) {
        super(httpServer);
    }

    @Override
    protected void handle() throws IOException {
        System.out.println("help");
        String response = "Server is running\n\n" +
                "Available methods:\n" +
                "/help\n" +
                "/mouse/move  params: dx, dy\n" +
                "/mouse/press  params: button\n" +
                "/mouse/release  params: button\n" +
                "/mouse/wheel  params: direction, amount\n";
        sendResponse(200,response);
    }

}