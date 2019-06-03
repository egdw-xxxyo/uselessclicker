package org.dikhim.jclicker.global;

import java.awt.*;

public class Mouse {
    public int getX() {
        return MouseInfo.getPointerInfo().getLocation().x;
    }

    public int getY() {
        return MouseInfo.getPointerInfo().getLocation().y;
    }
    
    public Point getLocation() {
        return new Point(getX(), getY());
    }
}
