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
        switch (direction.toUpperCase()) {
            case "UP":
                getHttpClient().getComputerObject().getMouseObject().wheelUp(amount);
                break;
            case "DOWN":
                getHttpClient().getComputerObject().getMouseObject().wheelDown(amount);
                break;
        }

        sendResponse(200,"Wheeled direction=" + direction + " amount=" + amount);
    }
}