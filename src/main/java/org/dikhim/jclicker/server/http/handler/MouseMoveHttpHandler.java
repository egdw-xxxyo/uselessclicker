package org.dikhim.jclicker.server.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.dikhim.jclicker.jsengine.objects.ComputerObject;
import org.dikhim.jclicker.util.WebUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class MouseMoveHttpHandler implements HttpHandler {

    private ComputerObject computerObject;

    public MouseMoveHttpHandler(ComputerObject computerObject) {
        this.computerObject = computerObject;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) return;
        Map<String, String> params = WebUtils.queryToMap(httpExchange.getRequestURI().getQuery());
        int dx = Integer.parseInt(params.get("dx"));
        int dy = Integer.parseInt(params.get("dy"));
        computerObject.getMouseObject().move(dx, dy);

        String response = "Moved dx="+dx+" dy="+dy;
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}