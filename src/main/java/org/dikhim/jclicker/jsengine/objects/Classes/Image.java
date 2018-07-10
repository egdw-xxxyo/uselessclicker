package org.dikhim.jclicker.jsengine.objects.Classes;

import org.dikhim.jclicker.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Image extends BufferedImage {
    private Map<Integer, List<Point>> pixels = new HashMap<>();
    

    public Image(int width, int height, int imageType) {
        super(width, height, imageType);
        loadPixels();
    }

    public Image(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
        loadPixels();
    }

    public Image(BufferedImage bufferedImage) {
        super(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        ImageUtil.drawImage(bufferedImage, this);
        loadPixels();
    }

    
    // private
    private void loadPixels() {
        int w = this.getWidth();
        int h = this.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int rgb = this.getRGB(i, j);
                if (pixels.containsKey(rgb)) {
                    pixels.get(rgb).add(new Point(i, j));
                } else {
                    List<Point> points = new ArrayList<>();
                    points.add(new Point(i, j));
                    pixels.put(rgb, points);
                }
            }
        }
    }
    
    
}
