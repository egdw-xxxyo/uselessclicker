package org.dikhim.jclicker.server.http.handler;

import org.dikhim.jclicker.server.http.HttpServer;

import java.io.IOException;

public class MouseWheelHttpHandler extends DefaultHttpHandler{
    public MouseWheelHttpHandler(HttpServer httpServer) {
        super(httpServer);
    }

    @Override
    protected void handle() throws IOException {
        String direction = getStringParam("direction");
        int amount = getIntParam("amount");
        getHttpClient().getComputerObject().getMouseObject().wheel(direction, amount);

        sendResponse(200,"Wheeled direction=" + direction + " amount=" + amount);
    }
}