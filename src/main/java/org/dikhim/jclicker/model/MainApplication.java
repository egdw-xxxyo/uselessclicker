package org.dikhim.jclicker.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.jsengine.clickauto.UselessClickAuto;
import org.dikhim.jclicker.server.http.HttpServer;
import org.dikhim.jclicker.server.socket.SocketServer;
import org.dikhim.jclicker.util.Out;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.function.Consumer;

public class MainApplication {
    private MainConfiguration config;


    private StringProperty title = new SimpleStringProperty("");
    private StringProperty status = new SimpleStringProperty("");

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

        config = Dependency.getConfig();
        httpServer = new HttpServer(config.getServers().getServer("httpServer"));
        socketServer = new SocketServer(config.getServers().getServer("socketServer"));
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
    }

    public void stop() {
        stopScript();
        socketServer.stop();
        httpServer.stop();
    }

    private void bindProperties() {
        title.bind(script.nameProperty());
        status.bind(Bindings.concat(script.nameProperty()).concat(" running:").concat(clickAuto.isRunningProperty()));
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

    public MainConfiguration getConfig() {
        return config;
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
