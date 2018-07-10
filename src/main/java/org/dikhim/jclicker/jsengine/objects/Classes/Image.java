package org.dikhim.jclicker.jsengine.objects.Classes;

import org.dikhim.jclicker.util.ImageUtil;

import java.awt.image.BufferedImage;

public class Image extends BufferedImage {
    public Image(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public Image(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public Image(BufferedImage bufferedImage) {
        super(bufferedImage.getWidth(), bufferedImage.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        ImageUtil.drawImage(bufferedImage,this);
    }
}
