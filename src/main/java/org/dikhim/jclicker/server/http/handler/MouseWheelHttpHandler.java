package org.dikhim.jclicker.server.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.dikhim.jclicker.jsengine.objects.ComputerObject;
import org.dikhim.jclicker.util.WebUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class MouseWheelHttpHandler implements HttpHandler {

    private ComputerObject computerObject;

    public MouseWheelHttpHandler(ComputerObject computerObject) {
        this.computerObject = computerObject;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        if (query == null) return;
        Map<String, String> params = WebUtils.queryToMap(httpExchange.getRequestURI().getQuery());
        String direction = params.get("direction");
        int amount = Integer.parseInt(params.get("amount"));
        computerObject.getMouseObject().wheel(direction, amount);

        String response = "Wheeled direction=" + direction + " amount=" + amount;
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}