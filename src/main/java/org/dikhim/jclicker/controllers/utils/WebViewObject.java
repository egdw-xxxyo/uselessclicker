package org.dikhim.jclicker.controllers.utils;

import org.dikhim.jclicker.jsengine.objects.ClipboardObject;
import org.dikhim.jclicker.jsengine.objects.JsClipboardObject;
import org.dikhim.jclicker.jsengine.robot.RobotStatic;

import java.util.function.Consumer;

public class WebViewObject {
    private Consumer<String> openInBrowser;
    private Consumer<String> onSetText;
    private ClipboardObject clipboardObject = new JsClipboardObject(RobotStatic.get());

    public WebViewObject() {
    }

    public void openInBrowser(String uri){
        openInBrowser.accept(uri);
    }

    public void hello(String text) {
        System.out.println("Hello "+text);
    }

    public void copy(String text) {
        System.out.println("copy");
        clipboardObject.set(text);
    }

    public void set(String text) {
        System.out.println("set");

        onSetText.accept(text);
    }

    public void setOpenInBrowser(Consumer<String> openInBrowser) {
        this.openInBrowser = openInBrowser;
    }

    public void setOnSetText(Consumer<String> onSetText) {
        this.onSetText = onSetText;
    }
}
