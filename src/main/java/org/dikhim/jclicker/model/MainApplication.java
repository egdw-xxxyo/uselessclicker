package org.dikhim.jclicker.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.jsengine.objects.ComputerObject;
import org.dikhim.jclicker.server.Server;
import org.dikhim.jclicker.server.http.HttpServer;
import org.dikhim.jclicker.server.socket.SocketServer;
import org.dikhim.jclicker.util.output.Out;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.script.ScriptException;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Duplicates")
public class MainApplication {
    MainConfiguration config;


    private StringProperty title = new SimpleStringProperty("");
    private StringProperty status = new SimpleStringProperty("");
    
    private HttpServer httpServer;
    private SocketServer socketServer;
    
    private Robot robot;
    private JSEngine jse;

    private Script script = new Script();

    public Script getScript() {
        return script;
    }

    public MainApplication() throws FileNotFoundException, URISyntaxException {
        jNativeHookStart();
        createRobot();
        jse = new JSEngine(robot);
        bindProperties();

        InputStream is = getClass().getResourceAsStream("/config.json");
        JsonReader jsonReader = Json.createReader(is);
        JsonObject jsonObject = jsonReader.readObject();
        config = new MainConfiguration(jsonObject, "main");
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
        jse.putCode(script.codeProperty().get());
        try {
            jse.start();
        } catch (ScriptException e) {
            e.printStackTrace();
            Out.print(e.getMessage());
        }
    }

    /**
     * Stop running script
     */
    public void stopScript() {
        jse.stop();

    }

    public void stop() {
        stopScript();
        jNativeHookStop();
        socketServer.stop();
        httpServer.stop();
    }

    private void bindProperties() {
        title.bind(script.nameProperty());
        status.bind(Bindings.concat(script.nameProperty()).concat(" выполняется:").concat(jse.runningProperty()));
    }


    /**
     * Initialization of JNativeHook
     */
    private void jNativeHookStart() {
        // suppress logger of jNativeHook
        Logger logger = Logger
                .getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);

        // suppress output of jNativeHook
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        }));
        try {
            GlobalScreen.registerNativeHook();

            // restore system output
            System.setOut(oldOut);
        } catch (NativeHookException e) {
            // restore system output
            System.setOut(oldOut);

            System.out.println(e.getMessage());
            System.exit(-1);
        }

        MouseEventsManager mouseListener = MouseEventsManager.getInstance();
        GlobalScreen.addNativeMouseListener(mouseListener);
        GlobalScreen.addNativeMouseMotionListener(mouseListener);
        GlobalScreen.addNativeMouseWheelListener(mouseListener);
        KeyEventsManager keyListener = KeyEventsManager.getInstance();
        GlobalScreen.addNativeKeyListener(keyListener);
    }

    private void jNativeHookStop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            Out.println(e.getMessage());
        }
    }

    private void createRobot() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Program start failure");
            alert.setHeaderText("Can not create 'Robot' object ");
            alert.setContentText(
                    "It can occurs if you have no permission for it\n" + "message:\n" + e.getMessage());
            alert.showAndWait();
            try {
                Clicker.getApplication().stop();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    public JSEngine getJse() {
        return jse;
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
    
}
