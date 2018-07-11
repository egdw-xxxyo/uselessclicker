package org.dikhim.jclicker.util;

import java.awt.*;

public class ShapeUtil {
    public static Rectangle createRectangle(int x0, int y0, int x1, int y1) {
        
        if (x0 < x1 && y0 < y1 && x0 >= 0 && y0 >= 0)
            return new Rectangle(x0, y0, x1 - x0, y1 - y0);

        
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
}
