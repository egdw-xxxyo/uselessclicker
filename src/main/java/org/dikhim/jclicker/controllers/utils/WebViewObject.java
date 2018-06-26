package org.dikhim.jclicker.controllers.utils;

import org.dikhim.jclicker.jsengine.objects.ClipboardObject;
import org.dikhim.jclicker.jsengine.objects.JsClipboardObject;
import org.dikhim.jclicker.jsengine.robot.RobotStatic;

import java.util.function.Consumer;

@SuppressWarnings("unused")
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
        clipboardObject.set(text);
    }

    public void set(String text) {
        onSetText.accept(text);
    }

    public void setOpenInBrowser(Consumer<String> openInBrowser) {
        this.openInBrowser = openInBrowser;
    }

    public void setOnSetText(Consumer<String> onSetText) {
        this.onSetText = onSetText;
    }
}
