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
        switch (button.toUpperCase()) {
            case "LEFT":
                getHttpClient().getComputerObject().getMouseObject().releaseLeft();
                break;
            case "RIGHT":
                getHttpClient().getComputerObject().getMouseObject().releaseRight();
                break;
            case "MIDDLE":
                getHttpClient().getComputerObject().getMouseObject().releaseMiddle();
                break;
        }
        sendResponse(200,"Released button=" + button);
    }
}