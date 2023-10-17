package org.dikhim.clickauto.jsengine.utils.image;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Pixels {
    private Map<Integer, ColorInfo> pixelMap = new HashMap<>();

    private List<ColorInfo> sortedList;


    public void add(int rgb, Point point) {
        ColorInfo colorInfoOpt1 = pixelMap.get(rgb);
        if (colorInfoOpt1 == null) {
            pixelMap.put(rgb, new ColorInfo(rgb, point));
        } else {
            colorInfoOpt1.addPoint(point);
        }
    }

    public void complete() {
        sortedList = new ArrayList<>();

        sortedList = pixelMap
                .values()
                .stream()
                .sorted(Comparator.comparingInt(ColorInfo::size))
                .collect(Collectors.toList());
    }

    public List<Point> getForColor(int rgb) {
        return pixelMap.get(rgb).getColorBlocks();
    }

    public List<ColorInfo> getPixels() {
        if (sortedList == null) complete();

        return sortedList;
    }
}
