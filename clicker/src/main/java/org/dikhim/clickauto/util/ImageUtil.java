package org.dikhim.clickauto.util;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public static void drawImage(BufferedImage inputImage, BufferedImage outputImage) {
        outputImage.createGraphics().drawImage(inputImage,0,0,inputImage.getWidth(), inputImage.getHeight(),null);
    }

    public static BufferedImage crop(BufferedImage originalImage, int top, int right, int bottom, int left) {
        int width = originalImage.getWidth() - left - right;
        int height= originalImage.getHeight() - top - bottom;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g = newImage.createGraphics();
        g.drawImage(originalImage, -left, -top, originalImage.getWidth(), originalImage.getHeight(), null);
        return newImage;
    }

    public static byte[] getByteArray(BufferedImage originalImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( originalImage, "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public static BufferedImage imageFromByteArray(byte[] data) throws IOException {
        InputStream is = new ByteArrayInputStream(data);
        return new Image(ImageIO.read(is));
    }
}
