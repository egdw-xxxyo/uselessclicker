package org.dikhim.jclicker.server.http.handler;

import org.dikhim.jclicker.server.http.HttpServer;

import java.io.IOException;

public class MouseMoveHttpHandler extends DefaultHttpHandler {

    public MouseMoveHttpHandler(HttpServer httpServer) {
        super(httpServer);
    }

    @Override
    protected void handle() throws IOException {
        int dx = getIntParam("dx");
        int dy = getIntParam("dy");
        getHttpClient().getComputerObject().getMouseObject().move(dx, dy);

        String response = "Moved dx=" + dx + " dy=" + dy;
        sendResponse(200, response);
    }
}