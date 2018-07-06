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

    public void captureImage(int x0, int y0, int x1, int y1) {
        if(isLocked()) return;
        Platform.runLater(() -> {
            locked = true;
            BufferedImage bufferedImage = screenObject.getImageWithFilledBlank(x0, y0, x1, y1);
            bufferedImage = drawCursorInTheMiddle(bufferedImage);
            
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

    private BufferedImage drawCursorInTheMiddle(BufferedImage inputImage) {
        
        int centerX = (inputImage.getWidth() - inputImage.getMinX()) / 2;
        int centerY = (inputImage.getHeight() - inputImage.getMinY()) / 2;
        int[][] cursorArray = {
                {0,1,1,1,1,1,1,0},
                {1,1,0,0,0,0,0,0},
                {1,0,1,0,0,0,0,0},
                {1,0,0,1,0,0,0,0},
                {1,0,0,0,1,0,0,0},
                {1,0,0,0,0,1,0,0},
                {1,0,0,0,0,0,1,0},
                {0,0,0,0,0,0,0,1}
        };

        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(cursorArray[j][i] !=1)continue;
                int x = centerX + i;
                int y = centerY + j;
                if (x >= inputImage.getWidth() || y>= inputImage.getHeight()) continue;
                
                
                Color color = new Color(inputImage.getRGB(x, y));

                float[] hsbVals = {0, 0, 0};
                Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsbVals);
                hsbVals[0] = 0f;
                hsbVals[1] = 0f;
                
                // invert Brightness
                if (hsbVals[2] > 0.5f) {
                    hsbVals[2] = 0.3f;
                } else {
                    hsbVals[2] = 0.7f;
                }
                
                int newColor =  Color.HSBtoRGB(hsbVals[0],hsbVals[1],hsbVals[2]);
                inputImage.setRGB(x, y,newColor);
            }
        }
        return inputImage;
    }
}
