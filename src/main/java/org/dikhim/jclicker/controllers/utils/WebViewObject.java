package org.dikhim.jclicker.controllers.utils;

import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.jsengine.clickauto.objects.ClipboardObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.UselessClipboardObject;

import java.util.function.Consumer;

public class WebViewObject {
    private Consumer<String> openInBrowser;
    private Consumer<String> onSetText;
    private Consumer<String> onRun;
    private ClipboardObject clipboardObject = new UselessClipboardObject(Dependency.getRobot());

    /**
     * Object of that class putted into html documentation to call some methods in java from it
     */
    public WebViewObject() {
    }

    /**
     * Opens link a default system browser
     * @param uri link
     */
    public void openInBrowser(String uri){
        openInBrowser.accept(uri);
    }

    /**
     * Copies text into clipboard
     * @param text text
     */
    public void setClipboard(String text) {
        clipboardObject.set(text);
    }

    /**
     * Sets code into code area. All that was there before will be deleted
     * @param code script
     */
    public void setToCodeTextArea(String code) {
        onSetText.accept(code);
    }
    /**
     * Sets code into code area and runs it immediately. All that was there before will be deleted
     * @param code script
     */
    public void runCode(String code) {
        onRun.accept(code);
    }

    public void setOpenInBrowser(Consumer<String> openInBrowser) {
        this.openInBrowser = openInBrowser;
    }

    public void setOnSetText(Consumer<String> onSetText) {
        this.onSetText = onSetText;
    }

    public void setOnRun(Consumer<String> onRun) {
        this.onRun = onRun;
    }
}
