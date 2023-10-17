package org.dikhim.clickauto.jsengine.objects.Classes;

import org.dikhim.clickauto.jsengine.utils.image.ImageFinder;
import org.dikhim.clickauto.jsengine.utils.image.Pixels;
import org.dikhim.clickauto.util.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Image extends BufferedImage {
    protected Pixels pixelsHolder = new Pixels();

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
                pixelsHolder.add(getRGB(i, j), new Point(i, j));
            }
        }
        pixelsHolder.complete();
    }

    public boolean isCompiled() {
        return !pixelsHolder.getPixels().isEmpty();
    }

    public List<Point> findAll(Image image) {
        return ImageFinder.findAll(this, image);
    }

    public List<Point> findAllCenter(Image image) {
        return ImageFinder.findAllCenter(this, image);
    }

    public Point findFirst(Image image) {
        return ImageFinder.findFirst(this, image);
    }

    public Point findFirstCenter(Image image) {
        return ImageFinder.findFirstCenter(this, image);
    }

    public List<Point> findLimit(Image image, int limit) {
        return ImageFinder.findLimit(this, image, limit);
    }

    public List<Point> findLimitCenter(Image image, int limit) {
        return ImageFinder.findLimitCenter(this, image, limit);
    }

    public Point matchBest(Image image) {
        return ImageFinder.matchBest(this, image);
    }

    public Point matchBest(Image image, Rectangle searchingArea) {
        return ImageFinder.matchBest(this, image, searchingArea);
    }

    public List<Point> matchThresholdLimit(Image image, double threshold, int limit){
        return ImageFinder.matchThresholdLimit(this, image, threshold, limit);
    }

    public List<Point> matchQuickThresholdLimit(Image image, double threshold, int limit){
        return ImageFinder.matchQuickThresholdLimit(this, image, threshold, limit);
    }

    public List<Point> matchQuickThresholdLimit(Image image, double threshold, int limit, double factor){
        return ImageFinder.matchQuickThresholdLimit(this, image, threshold, limit, factor);
    }

    public Pixels getPixelsHolder() {
        return pixelsHolder;
    }
}
