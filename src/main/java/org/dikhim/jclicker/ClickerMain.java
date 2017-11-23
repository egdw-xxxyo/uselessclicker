package org.dikhim.jclicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.ScriptException;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.text.Font;
import org.dikhim.jclicker.events.KeyEventsManager;
import org.dikhim.jclicker.events.MouseEventsManager;
import org.dikhim.jclicker.events.ShortcutEqualsHandler;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.model.Script;
import org.dikhim.jclicker.util.Cli;
import org.dikhim.jclicker.util.output.Out;
import org.dikhim.jclicker.util.output.SystemAndStringOutput;
import org.dikhim.jclicker.util.output.SystemOutput;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ClickerMain extends Application {
    private static ClickerMain application;

    private StringProperty titleProperty = new SimpleStringProperty();
    private Stage primaryStage;

    private MainController controller;

    private Robot robot;
    private JSEngine jse;
    private Script script;

    @Override
    public void start(Stage primaryStage) throws Exception {
        application = this;
        String filePath =  getParameters().getNamed().get("filePath");
        String type = getParameters().getNamed().get("type");
        this.primaryStage = primaryStage;
        jNativeHookStart();
        createRobot();
        createJSEngine();
        createScript();
        if(!filePath.equals(""))
            loadScript(filePath);
        if(type.equals("GUI")){
            startGuiApplication();
        }else if(type.equals("CLI")){
            startCliApplication();
        }


    }

    @Override
    public void stop() throws Exception {
        jse.stop();
        jNativeHookStop();
        super.stop();
    }

    private void startGuiApplication() throws Exception {
        loadMainScene();

        SystemAndStringOutput out = new SystemAndStringOutput();
        controller.bindOutputProperty(out.getProperty());
        controller.bindScriptProperty(script.getStringProperty());
        Out.setOutput(out);

        KeyEventsManager keyListener = KeyEventsManager.getInstance();
        keyListener.addPressListener(
                new ShortcutEqualsHandler("stopScript", "CONTROL ALT S", () -> {
                    Platform.runLater(() ->{
                        controller.stopScript();
                    });
                }));
    }

    private void startCliApplication() {
        Out.setOutput(new SystemOutput());
        runScript();
    }
    public static ClickerMain getApplication() {
        return application;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Initialization of JNativeHook
     */
    public static void jNativeHookStart() {
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

    /**
     * Stops jNativeHook and killing its thread
     */
    public static void jNativeHookStop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            Out.println(e.getMessage());
        }
    }

    public void runScript() {
        if (script == null)
            return;
        jse.putCode(script.getStringProperty().get());
        try {
            jse.start();
        } catch (ScriptException e) {
            e.printStackTrace();
            Out.print(e.getMessage());
        }
    }

    public void stopScript() {
        jse.stop();
    }

    public void setScript(Script script) {
        StringProperty prop = this.script.getStringProperty();
        prop.set(script.getStringProperty().get());
        script.setStringProperty(prop);
        this.script = script;
        this.updateTitle();
    }

    public Script getScript() {
        return script;
    }

    public void loadScript(String filePath){
        script.loadScript(filePath);
        updateTitle();
    }
    public void newScript() {
        StringProperty prop;
        if (script != null) {
            prop = script.getStringProperty();
            prop.set("");
            script = new Script();
            script.setStringProperty(prop);
        } else {
            script = new Script();

        }
        updateTitle();
    }

    public void newScript(String path) {
        StringProperty prop;
        File file = new File(path);
        if (script != null) {
            prop = script.getStringProperty();
            prop.set("");
            script = new Script(file);
            script.setStringProperty(prop);
        } else {
            script = new Script(file);

        }
        updateTitle();

    }

    public void updateTitle() {

        titleProperty.set(script.getName());
    }

    public static void main(String[] args) {
        Cli cli = new Cli(args);
        String[] params = new String[2];
        params[0] = "--filePath=" + cli.getFilePath();
        if(cli.isConsole()){
            params[1]="--type=CLI";
        }else{
            params[1]= "--type=GUI";
        }
        launch(params);
    }

    ///////////////////////
    private void createRobot() throws Exception {
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
            stop();
        }
    }

    private void createJSEngine() {
        jse = new JSEngine(robot);
    }

    private void createScript() {
        script = new Script();
        updateTitle();
    }

    private void loadMainScene() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = null;
        try {
            root = fxmlLoader
                    .load(getClass().getResource("/MainScene.fxml").openStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


        primaryStage.getIcons().add(new Image(
                getClass().getResourceAsStream("/images/cursor.png")));
        primaryStage.setScene(new Scene(root, 800, 600));
        controller = fxmlLoader.getController();
        primaryStage.titleProperty().bindBidirectional(titleProperty);
        primaryStage.show();
    }

}
