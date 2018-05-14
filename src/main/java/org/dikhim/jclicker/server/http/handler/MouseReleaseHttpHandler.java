package org.dikhim.jclicker.server.http.handler;

import org.dikhim.jclicker.server.http.HttpServer;

import java.io.IOException;

public class MouseReleaseHttpHandler extends DefaultHttpHandler{
    public MouseReleaseHttpHandler(HttpServer httpServer) {
        super(httpServer);
    }

    @Override
    protected void handle() throws IOException {
        String button = getStringParam("button");
        getHttpClient().getComputerObject().getMouseObject().release(button);

        sendResponse(200,"Released button=" + button);
    }
}