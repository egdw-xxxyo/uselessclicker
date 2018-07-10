package org.dikhim.jclicker.jsengine.objects.generators;

import org.dikhim.jclicker.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.jsengine.objects.CreateObject;

import java.awt.*;

public class CreateObjectGenerator extends SimpleCodeGenerator implements CreateObject {
    public CreateObjectGenerator(int lineSize) {
        super("create", lineSize, CreateObject.class);
    }

    CreateObjectGenerator() {
        super("create", CreateObject.class);
    }

    @Override
    public Image image(int width, int height) {
        append(String.format("var image = create.image(%s, %s);\n", width, height));
        return null;
    }

    @Override
    public Image image(String zipBase64String) {
        append(String.format("var image = create.image('%s');\n", zipBase64String));
        return null;
    }

    @Override
    public Point point(int x, int y) {
        append(String.format("var point = create.point(%s, %s);\n", x, y));
        return null;
    }
}
