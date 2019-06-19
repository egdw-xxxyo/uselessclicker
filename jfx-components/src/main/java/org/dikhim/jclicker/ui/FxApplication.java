package org.dikhim.jclicker.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.dikhim.jclicker.ui.statuspane.StatusPane;

public class FxApplication extends javafx.application.Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        Control control = new StatusPane();
        AnchorPane.setBottomAnchor(control, 0.0);
        AnchorPane.setRightAnchor(control, 0.0);
        AnchorPane.setLeftAnchor(control, 0.0);
        AnchorPane.setTopAnchor(control, 0.0);
        root.getChildren().addAll(control);
        VBox.setVgrow(control, Priority.ALWAYS);
        Scene scene = new Scene(root, 320, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
