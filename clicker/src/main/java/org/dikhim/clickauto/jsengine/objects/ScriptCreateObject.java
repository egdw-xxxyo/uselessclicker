package org.dikhim.clickauto.jsengine.objects;


import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.clickauto.util.ShapeUtil;
import org.dikhim.clickauto.util.logger.ClickAutoLog;
import org.dikhim.clickauto.util.ZipBase64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ScriptCreateObject implements CreateObject {

    @Override
    public Image image(int width, int height) {
        return new Image(width, height);
    }

    @Override
    public Image image(String zipBase64String) {
        try {
            // replace all unnecessary chars except base64 string
            zipBase64String = zipBase64String.replaceAll("^'|.*\\('|[\\n\\r *]+|'\\)?;?$|'[+\\n\\r ]+'", "");
            byte[] data = ZipBase64.decode(zipBase64String);
            InputStream is = new ByteArrayInputStream(data);
            return new Image(ImageIO.read(is));
        } catch (Exception e) {
            ClickAutoLog.get().error("Cannot create image from the string");
            return null;
        }
    }

    @Override
    public Image image(BufferedImage bufferedImage) {
        return new Image(bufferedImage);
    }

    @Override
    public Image imageFile(String path) {
        try {
            return new Image(ImageIO.read(new File(path)));
        } catch (IOException e) {
            ClickAutoLog.get().error("Cannot create image from filePath:"+path);
            return null;
        }
    }

    @Override
    public Point point(int x, int y) {
        return new Point(x, y);
    }

    @Override
    public Rectangle rectangle(int x, int y, int width, int height) {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle rectangle(Point point, int width, int height) {
        return new Rectangle(point.x, point.y, width, height);
    }

    @Override
    public Rectangle rectangle(Point p1, Point p2) {
        return ShapeUtil.createRectangle(p1, p2);
    }
}