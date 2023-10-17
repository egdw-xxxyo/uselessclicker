package org.dikhim.clickauto.jsengine.utils.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageConverter {
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }


    public static BufferedImage resizeImageSmooth(BufferedImage image, double factor) {
        return toBufferedImage(
                image.getScaledInstance(
                        (int) (image.getWidth() * factor),
                        (int) (image.getHeight() * factor),
                        Image.SCALE_AREA_AVERAGING));
    }
}
