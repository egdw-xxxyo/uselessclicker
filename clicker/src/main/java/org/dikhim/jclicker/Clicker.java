package org.dikhim.jclicker;

import javafx.application.Application;
import javafx.stage.Stage;
import org.dikhim.clickauto.ClickAuto;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.util.Cli;
import org.dikhim.jclicker.util.Out;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.prefs.Preferences;

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

        if (cli.isEventRecording()) {
            jNativeHookStart();
        }
        
        if (cli.isGuiApplication()) {
            if(Preferences.userRoot().node("main").getBoolean("isFirstLaunch",true)){
                Preferences.userRoot().node("main").putBoolean("isFirstLaunch",false);
                String language = WindowManager.showChooseLanguageDialog();
                Dependency.getConfig().localization().setApplicationLanguageId(language);
                Dependency.getConfig().save();
            }
            loadMainScene();
        }
        Out.addPrintMethod(System.out::print);
        ClickAuto.getLogger().addOutHandler(Out::print);
        ClickAuto.getLogger().addErrorHandler(Out::print);
        if (cli.isOpenFile()) {
            mainApplication.openFile(cli.getFile());
        }

        if (cli.isRunFile()) {
            mainApplication.openFile(cli.getFile());
            mainApplication.runScript();
        }

        if (cli.isRunHttpServer()) {
            mainApplication.getHttpServer().setPort(cli.getHttpPort());
            mainApplication.getHttpServer().start();
        }

        if (cli.isRunSocketServer()) {
            mainApplication.getSocketServer().setPort(cli.getSocketPort());
            mainApplication.getSocketServer().start();
        }

    }

    @Override
    public void stop() throws Exception {
        mainApplication.stop();
        if (cli.isEventRecording()) {
            jNativeHookStop();
        }
        super.stop();
    }

    private void loadMainScene() {
        Stage stage = WindowManager.getInstance().getStage("main");
        stage.titleProperty().bindBidirectional(mainApplication.titleProperty());
        stage.show();
    }

    /**
     * Initialization of JNativeHook
     */
    private void jNativeHookStart() {
        EventManager eventManager = new EventManager();
        Dependency.setEventManager(eventManager);
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

    public void openInBrowser(String uri) {
        getHostServices().showDocument(uri);
    }
}
