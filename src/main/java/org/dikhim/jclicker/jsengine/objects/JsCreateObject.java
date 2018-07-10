package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.util.Out;
import org.dikhim.jclicker.util.ZipBase64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsCreateObject implements CreateObject {
    @Override
    public Image image(int width, int height) {
        return new Image(width, height);
    }

    @Override
    public Image image(String zipBase64String) {
        try {
            byte[] data = ZipBase64.decode(zipBase64String);
            InputStream is = new ByteArrayInputStream(data);
            return new Image(ImageIO.read(is));
        } catch (Exception e) {
            Out.println("Cannot read create from the string");
            return null;
        }
    }

    @Override
    public Point point(int x, int y) {
        return new Point(x, y);
    }
}
