package org.dikhim.jclicker.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return resizedImage;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, double scale) {
        int width = (int) (originalImage.getWidth() * scale);
        int height = (int) (originalImage.getHeight() * scale);
        return resizeImage(originalImage, width, height);
    }

    public static BufferedImage clone(BufferedImage originalImage) {
        BufferedImage newImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        Graphics2D g = newImage.createGraphics();
        g.drawImage(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), null);
        return newImage;
    }

    public static BufferedImage crop(BufferedImage originalImage, int top, int right, int bottom, int left) {
        int width = originalImage.getWidth() - left - right;
        int height= originalImage.getHeight() - top - bottom;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = newImage.createGraphics();
        g.drawImage(originalImage, left, top, width, height, null);
        return newImage;
    }
}
