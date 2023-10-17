package org.dikhim.clickauto.jsengine.utils.image;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.clickauto.jsengine.utils.LimitedPriorityQueue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImageFinder {
    /**
     * Return the best matchBest for the template
     *
     * @param parent   parent image
     * @param template image that should be found
     * @return the Point object, that represents the best matchBest position
     */
    public static Point matchBest(BufferedImage parent, BufferedImage template) {
        validateParentTemplate(parent, template);
        return bestMatch(parent, template, new Rectangle(0, 0, parent.getWidth(), parent.getHeight())).getLocation();
    }

    /**
     * Return the best matchBest for the template in a specified rectangular area
     *
     * @param parent    parent image
     * @param template  image that should be found
     * @param rectangle represents an area where the method should find a template
     * @return the Point object, that represents the best matchBest position
     */
    public static Point matchBest(BufferedImage parent, BufferedImage template, Rectangle rectangle) {
        validateParentTemplateRectangle(parent, template, rectangle);
        return bestMatch(parent, template, rectangle).getLocation();
    }


    public static List<Point> matchThresholdLimit(BufferedImage parent, BufferedImage template, double threshold, int limit) {
        validateParentTemplate(parent, template);
        validateThreshold(threshold);
        validateLimit(limit);
        int dx = parent.getWidth() - template.getWidth();
        int dy = parent.getHeight() - template.getHeight();

        double[][] diffMap = new double[dx][dy];
        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {

                double diff = compare(parent, template, i, j);
                diffMap[i][j] = diff;
            }
        }
        //
        LimitedPriorityQueue<FoundArea> queue = new LimitedPriorityQueue<>(limit * 10);

        // mapFilter

        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {
                if (diffMap[i][j] < threshold) {
                    int x = i;
                    int y = j;
                    double diff = diffMap[i][j];
                    boolean first = true;
                    for (int k = i; k < Math.min(dx, i + template.getWidth()); k++) {
                        for (int l = j; l < Math.min(dy, j + template.getHeight()); l++) {

                            if (!first) {
                                if (diffMap[k][l] < threshold) {
                                    if (diffMap[k][l] <= diff) {
                                        diffMap[x][y] = 2;
                                        x = k;
                                        y = l;
                                        diff = diffMap[k][l];
                                    } else {
                                        x = k;
                                        y = l;
                                        diffMap[x][y] = 2;
                                    }
                                }
                            } else {
                                first = false;
                                x = k;
                                y = l;
                                diff = diffMap[k][l];
                            }

                        }
                    }
                    queue.add(new FoundArea(new Point(x, y), template.getWidth(), template.getHeight(), diff));
                }
            }
        }
        List<FoundArea> list = new ArrayList<>(queue.toList());
        List<FoundArea> resultList = new ArrayList<>();
        while (list.size() > 0) {
            FoundArea area = list.get(0);
            List<FoundArea> intersectingAreas =
                    list.stream().filter(foundArea -> foundArea.isOverlapping(area)).collect(Collectors.toList());

            resultList.add(intersectingAreas.stream().max(FoundArea::compareTo).get());
            list.removeAll(intersectingAreas);
        }
        return resultList.stream().map(Rectangle::getLocation).limit(limit).collect(Collectors.toList());
    }

    public static List<Point> matchQuickThresholdLimit(BufferedImage parent, BufferedImage template, double threshold, int limit) {
        validateParentTemplate(parent, template);
        validateThreshold(threshold);
        validateLimit(limit);
        return matchQuickThresholdLimit(parent, template, threshold, limit, 1.0);
    }

    public static List<Point> matchQuickThresholdLimit(BufferedImage parent, BufferedImage template, double threshold, int limit, double factor) {
        validateParentTemplate(parent, template);
        validateThreshold(threshold);
        validateLimit(limit);
        int dx = parent.getWidth() - template.getWidth();
        int dy = parent.getHeight() - template.getHeight();
        double tmpFactor = Math.max(1.0, Math.pow((long) dx * dy * template.getHeight() * template.getWidth() / 40000000.0, 0.25));
        double factor1 = Math.max(1.0, tmpFactor * factor);
        BufferedImage parentMini;
        BufferedImage templateMini;
        List<Point> points;
        if (factor1 < 1.05) {
            points = matchThresholdLimit(parent, template, threshold, (limit)).stream()
                    .map(point -> new Point((int) (point.x), (point.y)))
                    .collect(Collectors.toList());

        } else {
            parentMini = ImageConverter.resizeImageSmooth(parent, 1.0 / factor1);
            templateMini = ImageConverter.resizeImageSmooth(template, 1.0 / factor1);
            points = matchThresholdLimit(parentMini, templateMini, threshold, (int) (limit * factor1)).stream()
                    .map(point -> new Point((int) (point.x * factor1), (int) (point.y * factor1)))
                    .collect(Collectors.toList());
        }

        LimitedPriorityQueue<FoundArea> queue = new LimitedPriorityQueue<>(limit);

        int dPlus = (int) (factor1 * factor1);
        int dMinus = (int) (factor1 * factor1);
        points.forEach(point -> {
            int x1 = Math.max(point.x - dMinus, 0);
            int y1 = Math.max(point.y - dMinus, 0);
            int x2 = Math.min(point.x + template.getWidth() + dPlus, parent.getWidth());
            int y2 = Math.min(point.y + template.getHeight() + dPlus, parent.getHeight());
            Rectangle rect = new Rectangle(x1, y1, x2 - x1, y2 - y1);
            FoundArea p = bestMatch(parent, template, rect);
            queue.add(p);
        });

        return queue.toList()
                .stream()
                .map(FoundArea::getLocation)
                .collect(Collectors.toList());
    }


    //// Image
    public static List<Point> findAll(Image parent, Image image) {
        validateParentTemplate(parent, image);
        return findLimit(parent, image, Integer.MAX_VALUE);
    }

    public static List<Point> findAllCenter(Image parent, Image image) {
        validateParentTemplate(parent, image);
        return findAll(parent, image).stream()
                .peek(point -> {
                    point.x = point.x + image.getWidth() / 2;
                    point.y = point.y + image.getHeight() / 2;
                })
                .collect(Collectors.toList());
    }

    public static Point findFirst(Image parent, Image image) {
        validateParentTemplate(parent, image);
        List<Point> pointList = findLimit(parent, image, 1);
        if (!pointList.isEmpty()) {
            return pointList.get(0);
        } else {
            return new Point(-1, -1);
        }
    }

    public static Point findFirstCenter(Image parent, Image image) {
        validateParentTemplate(parent, image);
        Point point = findFirst(parent, image);
        point.x = point.x + image.getWidth() / 2;
        point.y = point.y + image.getHeight() / 2;
        return point;
    }

    public static List<Point> findLimit(Image parent, Image image, int limit) {
        validateParentTemplate(parent, image);
        validateLimit(limit);
        List<Point> resultList = new ArrayList<>();
        if (parent.isCompiled() && image.isCompiled()) {
            List<Point> possiblePositions = new ArrayList<>();
            List<ColorInfo> childColorInfoList = image.getPixelsHolder().getPixels();
            boolean first = true;
            outOfLoop:
            for (ColorInfo colorInfo : childColorInfoList) {
                // current color points
                List<Point> childPoints = colorInfo.getColorBlocks();
                // points for the color in the parent image
                List<Point> parentPoints = parent.getPixelsHolder().getForColor(colorInfo.getRgb());
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
                            return x >= parent.getWidth() || y >= parent.getHeight() || colorInfo.getRgb() != parent.getRGB(x, y);
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
            for (int i = 0; i < parent.getWidth(); i++) {
                for (int j = 0; j < parent.getHeight(); j++) {
                    // k,l - child image
                    boolean found = true;
                    parentLoop:
                    for (int k = 0; k < image.getWidth(); k++) {
                        for (int l = 0; l < image.getHeight(); l++) {
                            if (i + k >= parent.getWidth() || j + l >= parent.getHeight() || parent.getRGB(i + k, j + l) != image.getRGB(k, l)) {
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

    public static List<Point> findLimitCenter(Image parent, Image image, int limit) {
        validateParentTemplate(parent, image);
        validateLimit(limit);
        return findLimit(parent, image, limit).stream()
                .peek(point -> {
                    point.x = point.x + image.getWidth() / 2;
                    point.y = point.y + image.getHeight() / 2;
                })
                .collect(Collectors.toList());
    }


    ////////////////////////////////////////
    private static FoundArea bestMatch(BufferedImage parent, BufferedImage template, Rectangle rectangle) {
        int initX = (int) rectangle.getX();
        int initY = (int) rectangle.getY();
        int endX = (int) (rectangle.getWidth() - template.getWidth() + initX);
        int endY = (int) (rectangle.getHeight() - template.getHeight() + initY);
        FoundArea area = null;
        for (int i = initX; i <= endX; i++) {
            for (int j = initY; j < endY; j++) {
                double diff = compare(parent, template, i, j);
                if (area != null && area.getDiff() > diff) {
                    area.setLocation(i, j);
                    area.setDiff(diff);
                } else if (area == null) {
                    area = new FoundArea(new Point(i, j), template.getWidth(), template.getHeight(), diff);
                }
            }
        }
        return area;
    }

    private static double compare(BufferedImage parent, BufferedImage template, int dx, int dy) {
        long diff = 0;
        for (int i = 0; i < template.getWidth(); i++) {
            for (int j = 0; j < template.getHeight(); j++) {
                diff += comparePixels(template.getRGB(i, j), parent.getRGB(i + dx, j + dy));
            }
        }
        int avgDiff = (int) (diff / (template.getWidth() * template.getHeight()));
        return avgDiff / (double) 195075;
    }

    private static int comparePixels(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = (rgb1) & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = (rgb2) & 0xff;
        return (r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);
    }


    private static void validateParentTemplate(BufferedImage parent, BufferedImage template) {
        validateNotNull(parent, template);
        if (parent.getWidth() < template.getWidth() || parent.getHeight() < template.getHeight())
            throw new IllegalArgumentException("Template image should fit into");
    }

    private static void validateParentTemplateRectangle(BufferedImage parent, BufferedImage template, Rectangle searchingArea) {
        validateNotNull(parent, template, searchingArea);
        if (searchingArea.width < template.getWidth() || searchingArea.height < template.getHeight()) {
            throw new IllegalArgumentException("Template should fit into searching area");
        }
        if (searchingArea.x < 0
                || searchingArea.y < 0
                || searchingArea.getMaxX() > parent.getWidth()
                || searchingArea.getMaxY() > parent.getHeight()) {
            throw new IllegalArgumentException("Searching area should fit into parent image");
        }
    }

    private static void validateNotNull(Object... args) {
        if (args != null) {
            Arrays.stream(args).forEach(p -> {
                if (p == null) throw new IllegalArgumentException("Arguments cannot be null");
            });
        }
    }

    private static void validateLimit(int limit) {
        if (limit < 1) new IllegalArgumentException("The limit should not be less than 1");
    }

    private static void validateFactor(double factor) {
        if (factor < 1) new IllegalArgumentException("The factor should not be less than 0");
    }

    private static void validateThreshold(double theshold) {
        if (theshold < 0 || theshold > 1) new IllegalArgumentException("The threshold should be between 0 and 1");
    }
}
