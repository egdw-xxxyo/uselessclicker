package org.dikhim.jclicker.jsengine.objects.Classes;

import org.dikhim.jclicker.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Image extends BufferedImage {
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

    public List<Point> findAll(Image image) {
        return findLimit(image, Integer.MAX_VALUE);
    }

    public List<Point> findAllCenter(Image image) {
        return findAll(image).stream()
                .peek(point -> {
                    point.x = point.x + image.getWidth() / 2;
                    point.y = point.y + image.getHeight() / 2;
                })
                .collect(Collectors.toList());
    }

    public Point findFirst(Image image) {
        List<Point> pointList = findLimit(image, 1);
        if (!pointList.isEmpty()) {
            return pointList.get(0);
        } else {
            return new Point(-1, -1);
        }
    }

    public Point findFirstCenter(Image image) {
        Point point = findFirst(image);
        point.x = point.x + image.getWidth() / 2;
        point.y = point.y + image.getHeight() / 2;
        return point;
    }

    public List<Point> findLimit(Image image, int limit) {
        List<Point> resultList = new ArrayList<>();
        Point point = new Point(-1, -1);
        if (isCompiled() && image.isCompiled()) {
            List<Point> possiblePositions = new ArrayList<>();
            List<ColorInfo> childColorInfoList = image.getPx().pixels;
            boolean first = true;
            outOfLoop:
            for (ColorInfo colorInfo : childColorInfoList) {
                // current color points
                List<Point> childPoints = colorInfo.colorBlocks;
                // points for the color in the parent image
                List<Point> parentPoints = getPx().getForColor(colorInfo.rgb);
                if (parentPoints == null) break;

                for (Point p : childPoints) {
                    if (first) {
                        first = false;
                        // first fill the possiblePositions with 0 point in child object
                        for (Point parentPoint : parentPoints) {
                            int x = parentPoint.x - p.x;
                            int y = parentPoint.y - p.y;
                            if (x >= 0 && y >= 0) {
                                possiblePositions.add(new Point(x, y));
                            }
                        }
                    } else if (possiblePositions.isEmpty()) {
                        break outOfLoop;
                    } else {
                        // remove all wrong points
                        possiblePositions.removeIf(point1 -> {
                            // check the point from possible + coordinate inside current point
                            int x = point1.x + p.x;
                            int y = point1.y + p.y;
                            return x >= getWidth() || y >= getHeight() || colorInfo.rgb != getRGB(x, y);
                        });
                    }
                }
            }
            for (Point p : possiblePositions) {
                if (resultList.size() < possiblePositions.size() && resultList.size() < limit) {
                    resultList.add(p);
                } else {
                    break;
                }
            }
        } else {
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
                        resultList.add(new Point(i, j));
                        if (resultList.size() >= limit) break outOfLoop;

                    }
                }
            }
        }
        return resultList;
    }

    public List<Point> findLimitCenter(Image image, int limit) {
        return findLimit(image, limit).stream()
                .peek(point -> {
                    point.x = point.x + image.getWidth() / 2;
                    point.y = point.y + image.getHeight() / 2;
                })
                .collect(Collectors.toList());
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

    Pixels getPx() {
        return px;
    }
}
