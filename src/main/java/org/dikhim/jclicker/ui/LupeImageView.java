package org.dikhim.jclicker.ui;

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
import org.dikhim.jclicker.actions.MouseMoveHandler;
import org.dikhim.jclicker.actions.managers.MouseEventsManager;
import org.dikhim.jclicker.controllers.utils.ImageCapturer;
import org.dikhim.jclicker.jsengine.objects.JsScreenObject;
import org.dikhim.jclicker.jsengine.objects.ScreenObject;
import org.dikhim.jclicker.jsengine.robot.RobotStatic;
import org.dikhim.jclicker.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;

public class LupeImageView extends VBox implements Initializable {

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
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        AnchorPane.setBottomAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        
        // on change visibility hide/show parent and start/stop recording
        visibleProperty().addListener((observable, oldValue, newValue) -> {
            getParent().visibleProperty().bindBidirectional(this.visibleProperty());
            if (newValue) {
                start();
            } else {
                stop();
            } 
        });        
    }

    @FXML
    private ImageView lupeImage;

    private String prefix = "lupe";
    private IntegerProperty resolution = new SimpleIntegerProperty(33);

    @FXML
    void zoomIn(ActionEvent event) {
        int value = resolution.getValue();
        if (value > 9) {
            resolution.setValue(resolution.getValue() / 2 + 1);
        }
    }

    @FXML
    void zoomOut(ActionEvent event) {
        int value = resolution.getValue();
        if (value < 33) {
            resolution.setValue((resolution.getValue() - 1) * 2);
        }
    }

    private void setPreviewImage(BufferedImage bufferedImage) {
        int w = (int) lupeImage.getFitWidth();
        int h = (int) lupeImage.getFitHeight();
        BufferedImage resizedImage = ImageUtil.resizeImage(bufferedImage, w, h);
        Image image = SwingFXUtils.toFXImage(resizedImage, null);
        lupeImage.setImage(image);
    }

    private void start() {
        final ScreenObject screenObject = new JsScreenObject(RobotStatic.get());
        ImageCapturer imageCapturer = new ImageCapturer();
        imageCapturer.setScreenObject(screenObject);
        imageCapturer.setOnImageLoaded(this::setPreviewImage);

        BiConsumer<Integer, Integer> onMove = (x, y) -> {
            
        };

        MouseEventsManager mouseEventsManager = MouseEventsManager.getInstance();
        onMove.accept(mouseEventsManager.getX(), mouseEventsManager.getY());

        mouseEventsManager.addMoveListener(new MouseMoveHandler(prefix + ".show.on.move", e -> {
            if (!imageCapturer.isLocked()) {
                int x = e.getX();
                int y = e.getY();
                int rectSize = resolution.get();
                int x0 = x - rectSize / 2;
                int y0 = y - rectSize / 2;
                int x1 = x0 + rectSize;
                int y1 = y0 + rectSize;
                imageCapturer.captureImage(x0, y0, x1, y1);
            }
        }));
    }
    private void stop() {
        MouseEventsManager.getInstance().removeListenersByPrefix(prefix);
    }

}
