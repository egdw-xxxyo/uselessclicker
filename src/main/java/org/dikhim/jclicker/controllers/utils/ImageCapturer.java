package org.dikhim.jclicker.controllers.utils;

import javafx.application.Platform;
import org.dikhim.jclicker.jsengine.clickauto.objects.ScreenObject;
import org.dikhim.jclicker.util.RotatableSelector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.function.Consumer;

public class ImageCapturer {
    private ScreenObject screenObject;
    private Consumer<BufferedImage> onImageLoaded;
    private volatile boolean locked = false;

    private Color[] colors = {
            new Color(0, 0, 0),
            new Color(255, 0, 0),
            new Color(0, 255, 0),
            new Color(0, 0, 255)};
    private RotatableSelector<Color> colorSelector = new RotatableSelector<>(Arrays.asList(colors));


    private int[][][] cursors = {{
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    }, {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    }, {
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0}
    }};
    private RotatableSelector<int[][]> cursorSelector = new RotatableSelector<>(Arrays.asList(cursors));

    public void captureImage(Rectangle area) {
        this.area = area;
        captureImage();
    }


    public void update() {
        captureImage();
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

    public void changeColor() {
        colorSelector.rotate();
        update();
    }

    public void changeCursor() {
        cursorSelector.rotate();
        update();
    }

    private BufferedImage image;
    private Rectangle area;

    private void captureImage() {
        if (isLocked()) return;
        int x0 = area.getLocation().x;
        int y0 = area.getLocation().y;
        int x1 = x0 + area.width;
        int y1 = y0 + area.height;

        new Thread(() -> {
            locked = true;
            image = drawCursorInTheMiddle(screenObject.getFilledImage(x0, y0, x1, y1));
            onImageLoaded.accept(image);
            locked = false;
        }).start();
    }

    private BufferedImage drawCursorInTheMiddle(BufferedImage inputImage) {

        int centerX = (inputImage.getWidth() - inputImage.getMinX()) / 2;
        int centerY = (inputImage.getHeight() - inputImage.getMinY()) / 2;
        int[][] cursorArray = cursorSelector.getSelected();

        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (cursorArray[j][i] != 1) continue;
                int x = centerX + i - 5;
                int y = centerY + j - 5;
                if (x >= inputImage.getWidth() || y >= inputImage.getHeight()) continue;
                if (x < 0 || y < 0) continue;

                inputImage.setRGB(x, y, averageColor(colorSelector.getSelected(), new Color(inputImage.getRGB(x, y))).getRGB());
            }
        }
        return inputImage;
    }

    private Color averageColor(Color cursorColor, Color imageColor) {
        int r = (cursorColor.getRed() * 2 + imageColor.getRed()) / 3;
        int g = (cursorColor.getGreen() * 2 + imageColor.getGreen()) / 3;
        int b = (cursorColor.getBlue() * 2 + imageColor.getBlue()) / 3;
        return new Color(r, g, b);
    }
}
