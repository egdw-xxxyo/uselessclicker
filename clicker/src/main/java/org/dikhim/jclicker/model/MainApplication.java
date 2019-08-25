package org.dikhim.jclicker.model;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dikhim.clickauto.ClickAuto;
import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.configuration.hotkeys.HotKeys;
import org.dikhim.jclicker.eventmanager.listener.ShortcutPressListener;
import org.dikhim.jclicker.jsengine.clickauto.UselessClickAuto;
import org.dikhim.jclicker.server.http.HttpServer;
import org.dikhim.jclicker.server.socket.SocketServer;
import org.dikhim.jclicker.util.Out;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.function.Consumer;

public class MainApplication {


    private StringProperty title = new SimpleStringProperty("");
    private StringProperty status = new SimpleStringProperty("");

    private HttpServer httpServer;
    private SocketServer socketServer;

    private UselessClickAuto clickAuto;
    private Script script = new Script();

    private Consumer<BufferedImage> onSetOutputImage;


    public Script getScript() {
        return script;
    }

    public MainApplication() {
        clickAuto = new UselessClickAuto();
        bindProperties();

        httpServer = new HttpServer();
        socketServer = new SocketServer();

        createHotKeys();
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
        if (clickAuto.isRunning()) {
            clickAuto.stop();
        }
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
        return clickAuto.isRunningProperty();
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

    private void createHotKeys() {
        HotKeys hotKeys = Dependency.getConfig().hotKeys();

        StringProperty stopScriptShortcutStringProperty = new SimpleStringProperty("");
        stopScriptShortcutStringProperty.bindBidirectional(hotKeys.stopScript().keysProperty());

        Dependency.getEventManager().addListener(new ShortcutPressListener(
                "main.stopScript",
                () -> {
                    Platform.runLater(this::stopScript);
                },
                stopScriptShortcutStringProperty
        ));

        StringProperty runScriptShortcutStringProperty = new SimpleStringProperty("");
        runScriptShortcutStringProperty.bindBidirectional(hotKeys.runScript().keysProperty());

        Dependency.getEventManager().addListener(new ShortcutPressListener(
                "main.runScript",
                () -> {
                    Platform.runLater(this::runScript);
                },
                runScriptShortcutStringProperty
        ));
    }
}
