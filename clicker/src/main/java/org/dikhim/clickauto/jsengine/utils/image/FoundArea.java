package org.dikhim.clickauto.jsengine.utils.image;

import java.awt.*;

public class FoundArea extends Rectangle implements Comparable<FoundArea> {
    private double diff;

    public FoundArea(Point topLeft, Point bottomRight, double diff) {
        super(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
        this.diff = diff;
    }

    public FoundArea(Point point, int width, int height, double diff) {
        super(point.x, point.y, width, height);
        this.diff = diff;
    }

    public double getDiff() {
        return diff;
    }

    public void setDiff(double diff) {
        this.diff = diff;
    }

    public boolean isOverlapping(FoundArea other) {
        if (this.getMinY() > other.getMaxY() || other.getMinY() > this.getMaxY()) {
            return false;
        }
        if (this.getMinX() > other.getMaxX() || other.getMinX() > this.getMaxX()) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(FoundArea o) {
        double d = o.getDiff() - getDiff();
        if (d > 0) {
            return 1;
        } else if (d < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "FoundArea{" +
                "diff=" + diff +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}