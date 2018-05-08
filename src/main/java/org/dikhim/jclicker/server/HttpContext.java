package org.dikhim.jclicker.server;

import com.sun.net.httpserver.HttpHandler;

public class HttpContext {
    private String path;
    private HttpHandler handler;
    public HttpContext(String path, HttpHandler handler){
        this.path=path;
        this.handler=handler;
    }

    
    public String getPath(){
        return path;
    }

    public HttpHandler getHandler() {
        return handler;
    }
}
