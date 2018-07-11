package org.dikhim.jclicker.jsengine.objects.Classes;

import org.dikhim.jclicker.util.ImageUtil;
import org.dikhim.jclicker.util.Out;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Image extends BufferedImage {
    private Map<Integer, List<Point>> pixels = new HashMap<>();


    public Image(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public Image(int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    public Image(BufferedImage bufferedImage) {
        super(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        ImageUtil.drawImage(bufferedImage, this);
    }

    public void compile() {
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

    public boolean isCompiled() {
        return !pixels.isEmpty();
    }

    public Map<Integer, List<Point>> getPixels() {
        return pixels;
    }

    public Point findFirst(Image image) {
        Point point = null;
        if (!isCompiled() && !image.isCompiled()) {
            // i,j - parent image
            outOfLoop:
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    // k,l - child image
                    boolean found = true;
                    parentLoop:
                    for (int k = 0; k < image.getWidth(); k++) {
                        for (int l = 0; l < image.getHeight(); l++) {

                            if (i + k >= getWidth() || j + l >= getHeight() || getRGB(i + k, j + l) != image.getRGB(k, l)) {
                                found = false;
                                break parentLoop;
                            }
                        }
                    }
                    if (found) {
                        point = new Point(i, j);
                        break outOfLoop;
                    }
                }
            }
            return point;
        } 
        return null;
    }

}
