package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.eventmanager.event.KeyPressEvent;
import org.dikhim.jclicker.eventmanager.event.KeyReleaseEvent;
import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.objects.ScreenObject;
import org.dikhim.jclicker.jsengine.clickauto.objects.UselessScreenObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 * Recording buffered image
 */
public class ImageRecorder extends SimpleRecorder implements ScreenRecorder {
    private Point p1;
    ScreenObject screenObject = new UselessScreenObject(Dependency.getRobot());

    public ImageRecorder(Consumer<BufferedImage> onRecorded) {
        super(onRecorded);
    }

    @Override
    protected void onStart() {
        addListener(
                "recording.mouse.controlKey",
                new KeyPressReleaseListener(
                        "CONTROL",
                        (event) -> {
                            controlPressed(event);
                        },
                        (event) -> {
                            controlReleased(event);
                        }));
        
    }


    protected void controlPressed(KeyPressEvent event) {
        p1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }

    protected void controlReleased(KeyReleaseEvent event) {
        Point p2  = new Point(MouseInfo.getPointerInfo().getLocation().x+1, MouseInfo.getPointerInfo().getLocation().y+1);
        Image image = screenObject.getImage(p1, p2);
        putImage(image);
    }

    protected void putImage(BufferedImage image) {
        getOnRecorded().accept(image);
    }
}
