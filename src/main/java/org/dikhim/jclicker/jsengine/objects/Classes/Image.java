package org.dikhim.jclicker.jsengine.objects.Classes;

import java.awt.image.BufferedImage;

public class Image extends BufferedImage {
    public Image(int width, int height, int imageType) {
        super(width, height, imageType);
    }
    
    public Image(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_RGB);
    }
}
