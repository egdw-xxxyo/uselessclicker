package org.dikhim.jclicker.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.jsengine.clickauto.UselessClickAuto;
import org.dikhim.jclicker.server.http.HttpServer;
import org.dikhim.jclicker.server.socket.SocketServer;
import org.dikhim.jclicker.util.Out;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Consumer;

public class MainApplication {


    private StringProperty title = new SimpleStringProperty("");
    private StringProperty status = new SimpleStringProperty("");
    private BooleanProperty scriptRunning = new SimpleBooleanProperty(false);
    
    private HttpServer httpServer;
    private SocketServer socketServer;

    private UselessClickAuto clickAuto;

    private Consumer<BufferedImage> onSetOutputImage;

    private Script script = new Script();

    public Script getScript() {
        return script;
    }

    public MainApplication() throws AWTException {
        clickAuto = new UselessClickAuto();
        Dependency.setClickAuto(clickAuto);
        bindProperties();

        httpServer = new HttpServer();
        socketServer = new SocketServer();
    }

    public void newFile() {
        stopScript();
        script.newScriptFile();
    }

    public void openFile(File file) {
        stopScript();
        script.openScriptFile(file);
    }

    public void saveFile() {
        stopScript();
        script.saveScriptFile();
    }

    public void saveFileAs(File file) {
        stopScript();
        script.saveScriptFileAs(file);
    }

    /**
     * Evaluate code by JS Engine
     */
    public void runScript() {
        Out.clear();
        clickAuto.removeScripts();
        clickAuto.putScript(script.codeProperty().getValue());
        clickAuto.start();
    }

    /**
     * Stop running script
     */
    public void stopScript() {
        clickAuto.stop();
        Dependency.getEventManager().removeListenersWithPrefix("script.");
        Dependency.getEventManager().removeFiltersWithPrefix("script.");
    }

    public void stop() {
        stopScript();
        socketServer.stop();
        httpServer.stop();
    }

    private void bindProperties() {
        title.bind(script.nameProperty());
        status.bind(Bindings.concat(script.nameProperty()).concat(" running:").concat(clickAuto.isRunningProperty()));
        scriptRunning.bindBidirectional(clickAuto.isRunningProperty());
    }

    public UselessClickAuto getClickAuto() {
        return clickAuto;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public BooleanProperty scriptRunningProperty() {
        return scriptRunning;
    }

    public HttpServer getHttpServer() {
        return httpServer;
    }

    public SocketServer getSocketServer() {
        return socketServer;
    }

    public void setScript(String script) {
        this.script.codeProperty().setValue(script);
    }

    public Consumer<BufferedImage> getOnSetOutputImage() {
        return onSetOutputImage;
    }

    public void setOnSetOutputImage(Consumer<BufferedImage> onSetOutputImage) {
        this.onSetOutputImage = onSetOutputImage;
    }
}
