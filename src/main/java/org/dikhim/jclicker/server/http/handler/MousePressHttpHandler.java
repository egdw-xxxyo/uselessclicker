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
        switch (button.toUpperCase()) {
            case "LEFT":
                getHttpClient().getComputerObject().getMouseObject().pressLeft();
                break;
            case "RIGHT":
                getHttpClient().getComputerObject().getMouseObject().pressRight();
                break;
            case "MIDDLE":
                getHttpClient().getComputerObject().getMouseObject().pressMiddle();
                break;
        }        
        sendResponse(200,"Pressed button=" + button);
    }
}
