package org.dikhim.jclicker.controllers.utils;

import javafx.application.HostServices;
import org.dikhim.jclicker.jsengine.objects.ClipboardObject;
import org.dikhim.jclicker.jsengine.objects.JsClipboardObject;
import org.dikhim.jclicker.jsengine.objects.JsSystemObject;
import org.dikhim.jclicker.jsengine.objects.SystemObject;
import org.dikhim.jclicker.jsengine.robot.RobotStatic;

import java.util.function.Consumer;

public class WebViewObject {
    private Consumer<String> openInBrowser;
    private ClipboardObject clipboardObject = new JsClipboardObject(RobotStatic.get());

    public WebViewObject(Consumer<String> openInBrowser) {
        this.openInBrowser = openInBrowser;
    }

    public void openInBrowser(String uri){
        openInBrowser.accept(uri);
    }

    public void hello(String text) {
        System.out.println("Hello "+text);
    }

    public void copy(String text) {
        clipboardObject.set(text);
    }
}
