package org.dikhim.jclicker.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import org.dikhim.jclicker.ClickerMain;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.configuration.MainConfiguration;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.util.output.Out;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.script.ScriptException;
import java.awt.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("Duplicates")
public class MainApplication {
    MainConfiguration config;

    private StringProperty title = new SimpleStringProperty("");
    private StringProperty status = new SimpleStringProperty("");
    private Robot robot;
    private JSEngine jse;

    private Script script = new Script();
    public Script getScript() {
        return script;
    }

    public MainApplication() throws FileNotFoundException {
        jNativeHookStart();
        createRobot();
        jse = new JSEngine(robot);
        bindProperties();

        File file = new File(getClass().getResource("/config.json").getFile());
        config = new MainConfiguration(file, "main");
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
    }

    private void bindProperties() {
        title.bind(script.nameProperty());
        status.bind(Bindings.concat(script.nameProperty()).concat(" is running:").concat(jse.runningProperty()));
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
                ClickerMain.getApplication().stop();
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
}
