package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.jsengine.clickauto.objects.CreateObject;

import java.awt.*;

public class CreateObjectOldCodeGenerator extends SimpleOldCodeGenerator implements CreateObject {
    public CreateObjectOldCodeGenerator(int lineSize) {
        super("create", lineSize, CreateObject.class);
    }

    public CreateObjectOldCodeGenerator() {
        super("create", CreateObject.class);
    }

    @Override
    protected SimpleOldCodeGenerator append(String str) {
        getSb().append(str);
        return this;
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
    public Image imageFile(String path) {
        append(String.format("var image = create.imageFile('%s');\n", path));
        return null;
    }

    @Override
    public Point point(int x, int y) {
        append(String.format("var point = create.point(%s, %s);\n", x, y));
        return null;
    }
}
