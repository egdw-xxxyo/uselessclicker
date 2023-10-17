package org.dikhim.clickauto.jsengine.utils.image;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ColorInfo {
    private int rgb;
    private List<Point> colorBlocks;

    public ColorInfo(int rgb, Point point) {
        this.rgb = rgb;
        this.colorBlocks = new ArrayList<>();
        colorBlocks.add(point);
    }

    public void addPoint(Point point) {
        colorBlocks.add(point);
    }

    public int size() {
        return colorBlocks.size();
    }

    public int getRgb() {
        return rgb;
    }

    public List<Point> getColorBlocks() {
        return colorBlocks;
    }
}
