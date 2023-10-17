package org.dikhim.clickauto.util;

import java.awt.*;

public class ShapeUtil {
    public static Rectangle createRectangle(int x0, int y0, int x1, int y1) {
        int min = Math.min(x0, x1);
        int max = Math.max(x0, x1);
        x0 = min;
        x1 = max;

        min = Math.min(y0, y1);
        max = Math.max(y0, y1);
        y0 = min;
        y1 = max;

        return new Rectangle(x0, y0, x1 - x0, y1 - y0);
    }

    public static Rectangle createRectangle(Point p1, Point p2) {
        return createRectangle(p1.x, p1.y, p2.x, p2.y);
    }

    public static Rectangle createRectangleIncludesBorder(int x0, int y0, int x1, int y1) {
        Rectangle tempRect = createRectangle(x0, y0, x1, y1);

        tempRect.height++;
        tempRect.width++;

        return tempRect;
    }

    public static Rectangle createRectangleIncludesBorder(Point p1, Point p2) {
        return createRectangleIncludesBorder(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Creates rectangle that fits into parent rectangle and have at least 1 px width and height.
     * If rectangle doesn't fit it drops or creates a rectangle with 1 px w/h at the border
     *
     * @param parent parent rectangle
     * @param child  child rectangle
     * @return fitted rectangle
     */
    public static Rectangle createMinimalRectangleFitIntoParent(Rectangle parent, Rectangle child) {
        Point p1 = child.getLocation();
        Point p2 = new Point((int) child.getMaxX(), (int) child.getMaxY());
        
        if(p1.x < parent.x) p1.x = parent.x;
        if(p1.y < parent.y) p1.y= parent.y;
        if(p2.x <= parent.x) p2.x = parent.x+1;
        if(p2.y <= parent.y) p2.y= parent.y+1;
        if(p1.x >= parent.getMaxX()) p1.x = (int) parent.getMaxX()-1;
        if(p1.y >= parent.getMaxY()) p1.y = (int) parent.getMaxY()-1;
        if(p2.x > parent.getMaxX()) p2.x = (int) parent.getMaxX();
        if(p2.y > parent.getMaxY()) p2.y = (int) parent.getMaxY();
        
        return createRectangle(p1, p2);
    }


}
