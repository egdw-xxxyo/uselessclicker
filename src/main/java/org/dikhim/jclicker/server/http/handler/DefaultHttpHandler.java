package org.dikhim.jclicker.server.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.dikhim.jclicker.server.http.HttpClient;
import org.dikhim.jclicker.server.http.HttpServer;
import org.dikhim.jclicker.util.WebUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Map;

public abstract class DefaultHttpHandler implements HttpHandler {

    private HttpExchange httpExchange;
    private Map<String, String> params;
    private HttpServer httpServer;
    private HttpClient httpClient;

    public DefaultHttpHandler(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    private void loadParameters() {
        params = WebUtils.queryToMap(httpExchange.getRequestURI().getQuery());
    }

    private void connectClient() {
        int uid;
        try {
            uid = getIntParam("uid");
            httpClient = httpServer.getClientByUid(uid);
        } catch (Exception e) {
            httpClient = httpServer.getClientByUid(0);
        }
    }

    protected abstract void handle() throws IOException;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        this.httpExchange = httpExchange;

        loadParameters();
        connectClient();

        String response;
        try {
            handle();
        } catch (InvalidParameterException e) {
            response = e.getMessage();
            sendResponse(400,response);
        } catch (Exception e) {
            sendResponse(400,"Unknown error");
        }
    }


    public int getIntParam(String paramName) {
        try {
            return Integer.parseInt(params.get(paramName));
        } catch (Exception e) {
            throw new InvalidParameterException("Parameter '"+paramName+"' not found or has invalid value");
        }
    }

    public String getStringParam(String paramName) {
        return params.get(paramName);
    }

       
    public void sendResponse(int code, String response) throws IOException {
        httpExchange.sendResponseHeaders(code, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    HttpClient getHttpClient() {
        return httpClient;
    }

}
