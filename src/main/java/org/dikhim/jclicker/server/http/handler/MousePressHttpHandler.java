package org.dikhim.jclicker.server.http.handler;

import org.dikhim.jclicker.server.http.HttpServer;

import java.io.IOException;

public class MousePressHttpHandler extends DefaultHttpHandler {

    public MousePressHttpHandler(HttpServer httpServer) {
        super(httpServer);
    }

    @Override
    protected void handle() throws IOException {
        String button = getStringParam("button");
        getHttpClient().getComputerObject().getMouseObject().press(button);
        
        sendResponse(200,"Pressed button=" + button);
    }
}
