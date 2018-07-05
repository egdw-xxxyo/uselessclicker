package org.dikhim.jclicker.controllers.utils;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.dikhim.jclicker.jsengine.objects.ScreenObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class ImageCapturer {
    private Object monitor = new Object();
    private ScreenObject screenObject;
    private Consumer<BufferedImage> onImageLoaded;
    private boolean locked = false;

    public void captureImage(Rectangle rectangle) {
        if(isLocked()) return;
        Platform.runLater(() -> {
            locked = true;
            BufferedImage bufferedImage = screenObject.getImage(rectangle);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            onImageLoaded.accept(bufferedImage);
            locked = false;
        });
    } 
    public void captureImage(int x0, int x1, int y0, int y1) {
        if(isLocked()) return;
        Platform.runLater(() -> {
            locked = true;
            BufferedImage bufferedImage = screenObject.getImage(x0, x1, y0, y1);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            onImageLoaded.accept(bufferedImage);
            locked = false;
        });
    }

    public void setScreenObject(ScreenObject screenObject) {
        this.screenObject = screenObject;
    }

    public void setOnImageLoaded(Consumer<BufferedImage> onImageLoaded) {
        this.onImageLoaded = onImageLoaded;
    }

    public boolean isLocked() {
        return locked;
    }
}
