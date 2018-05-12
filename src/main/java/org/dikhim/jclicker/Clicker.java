package org.dikhim.jclicker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.dikhim.jclicker.model.MainApplication;
import org.dikhim.jclicker.server.socket.SocketServer;
import org.dikhim.jclicker.util.Cli;
import org.dikhim.jclicker.util.output.Out;
import org.dikhim.jclicker.util.output.SystemAndStringOutput;

import java.io.IOException;

public class Clicker extends Application {
    private static Clicker application;


    // main app
    private MainApplication mainApplication;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainApplication = new MainApplication();
        application = this;
        this.primaryStage = primaryStage;
        startGuiApplication();
    }

    @Override
    public void stop() throws Exception {
        mainApplication.stop();
        SocketServer.getInstance().stop();
        super.stop();
    }


    // ui
    private void startGuiApplication() throws Exception {
        Out.setOutput(new SystemAndStringOutput());
        loadMainScene();
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

    public static Clicker getApplication() {
        return application;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }


    public static void main(String[] args) {
        Cli cli = new Cli(args);
        String[] params = new String[2];
        params[0] = "--filePath=" + cli.getFilePath();
        if (cli.isConsole()) {
            params[1] = "--type=CLI";
        } else {
            params[1] = "--type=GUI";
        }
        launch(params);
    }

    public MainApplication getMainApplication() {
        return mainApplication;
    }
}
