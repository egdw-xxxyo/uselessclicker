package org.dikhim.jclicker.controllers.utils;

import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.jsengine.clickauto.objects.ClipboardObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.UselessClipboardObject;

import java.util.function.Consumer;

public class WebViewObject {
    private Consumer<String> openInBrowser;
    private Consumer<String> onSetText;
    private ClipboardObject clipboardObject = new UselessClipboardObject(Dependency.getRobot());

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
