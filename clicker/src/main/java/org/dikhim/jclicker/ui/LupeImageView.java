package org.dikhim.jclicker.ui;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.controllers.utils.recording.ImageCapturer;
import org.dikhim.jclicker.eventmanager.EventManager;
import org.dikhim.jclicker.eventmanager.event.MouseMoveEvent;
import org.dikhim.jclicker.eventmanager.listener.SimleMouseMoveListener;
import org.dikhim.jclicker.jsengine.clickauto.objects.ScreenObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.UselessScreenObject;
import org.dikhim.jclicker.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LupeImageView extends VBox implements Initializable {


    private IntegerProperty resolution = new SimpleIntegerProperty(33);
    private Position position = Position.RIGHT;
    private EventManager eventManager = Dependency.getEventManager();
    private ImageCapturer imageCapturer = new ImageCapturer();

    @FXML
    private ImageView lupeImage;

    public LupeImageView(ResourceBundle resources) {
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setClassLoader(getClass().getClassLoader());
        fxmlLoader.setResources(resources);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setLocation(getClass().getResource("/fxml/LupeImageView.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // on change visibility hide/show parent and start/stop recording
        visibleProperty().addListener((observable, oldValue, newValue) -> {
            getParent().visibleProperty().bind(this.visibleProperty());
            if (newValue) {
                start();
            } else {
                stop();
            }
        });

        // change position on click
        lupeImage.setOnMouseClicked(event -> {
            if (position == Position.LEFT) {
                position = Position.RIGHT;
                AnchorPane.setLeftAnchor(this.getParent(), null);
                AnchorPane.setRightAnchor(this.getParent(), 0d);
            } else {
                position = Position.LEFT;
                AnchorPane.setLeftAnchor(this.getParent(), 0d);
                AnchorPane.setRightAnchor(this.getParent(), null);
            }
        });
    }


    @FXML
    void zoomIn(ActionEvent event) {
        int value = resolution.getValue();
        if (value > 9) {
            resolution.setValue(resolution.getValue() / 2 + 1);
        }
        int initX = eventManager.getMouse().getX();
        int initY = eventManager.getMouse().getY();
        imageCapturer.captureImage(getCaptureRect(initX, initY));
    }

    @FXML
    void zoomOut(ActionEvent event) {
        int value = resolution.getValue();
        if (value < 33) {
            resolution.setValue((resolution.getValue() - 1) * 2);
        }
        int initX = eventManager.getMouse().getX();
        int initY = eventManager.getMouse().getY();
        imageCapturer.captureImage(getCaptureRect(initX, initY));
    }

    @FXML
    void changeCursor(ActionEvent event) {
        imageCapturer.changeCursor();
    }

    @FXML
    void changeColor(ActionEvent event) {
        imageCapturer.changeColor();
    }

    private void setPreviewImage(BufferedImage bufferedImage) {
        int w = (int) lupeImage.getFitWidth();
        int h = (int) lupeImage.getFitHeight();
        BufferedImage resizedImage = ImageUtil.resizeImage(bufferedImage, w, h);
        Image image = SwingFXUtils.toFXImage(resizedImage, null);
        Platform.runLater(() -> lupeImage.setImage(image));
    }

    private void start() {
        ScreenObject screenObject = new UselessScreenObject(Dependency.getRobot());
        imageCapturer.setScreenObject(screenObject);
        imageCapturer.setOnImageLoaded(this::setPreviewImage);

        int initX = eventManager.getMouse().getX();
        int initY = eventManager.getMouse().getY();
        imageCapturer.captureImage(getCaptureRect(initX, initY));

        eventManager.addListener(new SimleMouseMoveListener("lupe.image.view") {
            @Override
            public void mouseMoved(MouseMoveEvent event) {
                imageCapturer.captureImage(getCaptureRect(event.getX(), event.getY()));
            }
        });
    }

    private Rectangle getCaptureRect(int x, int y) {
        int rectSize = resolution.get();
        int x0 = x - rectSize / 2;
        int y0 = y - rectSize / 2;
        return new Rectangle(x0, y0, rectSize, rectSize);
    }

    private void stop() {
        eventManager.removeListener("lupe.image.view");
    }

    public enum Position {
        LEFT,
        RIGHT
    }
}
