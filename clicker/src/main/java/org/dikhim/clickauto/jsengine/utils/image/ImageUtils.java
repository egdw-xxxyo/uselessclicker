package org.dikhim.clickauto.jsengine.utils.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public BufferedImage openImage(String url) throws IOException {
        BufferedImage img = ImageIO.read(new File(url));
        return img;
    }

    public void saveImage(BufferedImage image, String url) throws IOException {
        ImageIO.write(image, "jpg", new File(url));
    }
}
