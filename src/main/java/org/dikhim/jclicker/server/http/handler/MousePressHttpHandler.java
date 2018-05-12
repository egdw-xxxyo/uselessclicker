package org.dikhim.jclicker.server.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.dikhim.jclicker.jsengine.objects.ComputerObject;
import org.dikhim.jclicker.util.WebUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class MousePressHttpHandler implements HttpHandler {
    private ComputerObject computerObject;

    public MousePressHttpHandler(ComputerObject computerObject) {
        this.computerObject = computerObject;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) return;
        Map<String, String> params = WebUtils.queryToMap(httpExchange.getRequestURI().getQuery());
        String button = params.get("button");
        computerObject.getMouseObject().press(button);

        String response = "Pressed button=" + button;
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
