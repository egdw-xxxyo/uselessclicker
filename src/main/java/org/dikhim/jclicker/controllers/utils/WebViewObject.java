package org.dikhim.jclicker.controllers.utils;

import javafx.application.HostServices;

import java.util.function.Consumer;

public class WebViewObject {
    private Consumer<String> openInBrowser;

    public WebViewObject(Consumer<String> openInBrowser) {
        this.openInBrowser = openInBrowser;
    }

    public void openInBrowser(String uri){
        openInBrowser.accept(uri);
    }

    public void hello(String text) {
        System.out.println("Hello "+text);
    }
        
}
