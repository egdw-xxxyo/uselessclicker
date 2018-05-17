package org.dikhim.jclicker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.util.Cli;
import org.dikhim.jclicker.util.Out;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Clicker extends Application {
    private static Clicker application;


    // main app
    private MainApplication mainApplication;

    private Stage primaryStage;
    private static Cli cli;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainApplication = new MainApplication();
        application = this;
        this.primaryStage = primaryStage;
        
        if(cli.isGuiApplication()){            
            loadMainScene();
        }
        Out.addPrintMethod(System.out::print);
        
        if(cli.isOpenFile()){
            mainApplication.openFile(cli.getFile());
        }
        
        if(cli.isRunFile()){
            mainApplication.openFile(cli.getFile());
            mainApplication.runScript();
        }
        
        if(cli.isRunHttpServer()){
            mainApplication.getHttpServer().setPort(cli.getHttpPort());
            mainApplication.getHttpServer().start();
        }

        if(cli.isRunSocketServer()){
            mainApplication.getSocketServer().setPort(cli.getSocketPort());
            mainApplication.getSocketServer().start();
        }

        if (cli.isEventRecording()) {
            jNativeHookStart();
        }
        
    }

    @Override
    public void stop() throws Exception {
        mainApplication.stop();
        if(cli.isEventRecording()){
            jNativeHookStop();
        }
        super.stop();
    }

    private void loadMainScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/main/MainScene.fxml"));
            primaryStage.getIcons().add(new Image(
                    getClass().getResourceAsStream("/images/cursor.png")));
            primaryStage.setScene(new Scene(root, 800, 600));

            primaryStage.titleProperty().bindBidirectional(mainApplication.titleProperty());
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            MouseEventsManager mouseListener = MouseEventsManager.getInstance();
            GlobalScreen.addNativeMouseListener(mouseListener);
            GlobalScreen.addNativeMouseMotionListener(mouseListener);
            GlobalScreen.addNativeMouseWheelListener(mouseListener);
            KeyEventsManager keyListener = KeyEventsManager.getInstance();
            GlobalScreen.addNativeKeyListener(keyListener); 
            System.setOut(oldOut);
        } catch (NativeHookException e) {
            System.setOut(oldOut);
            Out.println("Cannot create keyboard/mouse recording object");
            Out.println("Recording events won't be available");
        }       
    }

    private void jNativeHookStop() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            Out.println(e.getMessage());
        }
    }


    public static Clicker getApplication() {
        return application;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }


    public static void main(String[] args) {
        cli = new Cli(args);
        launch(args);
    }

    public MainApplication getMainApplication() {
        return mainApplication;
    }
}
