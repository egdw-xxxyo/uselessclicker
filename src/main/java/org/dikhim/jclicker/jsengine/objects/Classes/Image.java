package org.dikhim.jclicker.jsengine.objects.Classes;

import org.dikhim.jclicker.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.PipedOutputStream;
import java.util.*;
import java.util.List;

public class Image extends BufferedImage {
    private Map<Integer, List<Point>> pixels = new TreeMap<>();

    private Pixels px = new Pixels();

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
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                px.add(getRGB(i, j), new Point(i, j));
            }
        }
        px.pixels.sort(Comparator.comparingInt(ColorInfo::size));
    }

    public boolean isCompiled() {
        return !px.pixels.isEmpty();
    }

    public Map<Integer, List<Point>> getPixels() {
        return pixels;
    }

    public Point findFirst(Image image) {
        Point point = null;
        if (!isCompiled() && !image.isCompiled()) {
            long time = System.currentTimeMillis();
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
            System.out.println("uncompiled time:" + (System.currentTimeMillis() - time));
        } else if (isCompiled() && image.isCompiled()) {
            long time = System.currentTimeMillis();

            List<Point> possiblePositions = new ArrayList<>();

            List<ColorInfo> childColorInfoList = image.getPx().pixels;

            for (ColorInfo colorInfo : childColorInfoList) {

                // current color points
                List<Point> childPoints = colorInfo.colorBlocks;
                // points for the color in the parent image
                List<Point> parentPoints = getPx().getForColor(colorInfo.rgb);
                if (possiblePositions.isEmpty()) {
                    // first fill the possiblePositions with 0 point in child object
                    Point childPoint = childPoints.get(0);

                    for (Point parentPoint : parentPoints) {
                        int x = parentPoint.x - childPoint.x;
                        int y = parentPoint.y - childPoint.y;
                        if (x >= 0 && y >= 0) {
                            possiblePositions.add(new Point(x, y));
                        }
                    }
                }
                
            }


            System.out.println("compiled time:" + (System.currentTimeMillis() - time));
        }
        return point;
    }

    private static class ColorInfo {
        int rgb;
        private List<Point> colorBlocks;

        public ColorInfo(int rgb, Point point) {
            this.rgb = rgb;
            this.colorBlocks = new ArrayList<>();
            colorBlocks.add(point);
        }

        void addPoint(Point point) {
            colorBlocks.add(point);
        }

        int size() {
            return colorBlocks.size();
        }
    }

    private static class Pixels {
        List<ColorInfo> pixels = new ArrayList<>();

        void add(int rgb, Point point) {
            Optional<ColorInfo> colorInfoOpt = pixels.stream()
                    .filter(colorInfo -> colorInfo.rgb == rgb)
                    .findFirst();
            if (colorInfoOpt.isPresent()) {
                colorInfoOpt.get().addPoint(point);
            } else {
                pixels.add(new ColorInfo(rgb, point));
            }
        }

        List<Point> getForColor(int rgb) {
            return pixels.stream()
                    .filter(colorInfo -> colorInfo.rgb == rgb)
                    .findFirst()
                    .map(colorInfo2 -> colorInfo2.colorBlocks)
                    .orElse(null);
        }


    }

    public Pixels getPx() {
        return px;
    }
}
