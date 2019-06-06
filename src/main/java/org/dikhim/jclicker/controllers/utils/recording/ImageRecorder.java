package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.jclicker.Dependency;
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
                new KeyPressReleaseListener(
                        "recording.mouse.controlKey",
                        "CONTROL",
                        (event) -> startRecording(),
                        (event) -> stopRecording()));

    }

    @Override
    protected void onRecordingStarted() {
        p1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);

    }

    @Override
    protected void onRecordingStopped() {
        Point p2 = new Point(MouseInfo.getPointerInfo().getLocation().x + 1, MouseInfo.getPointerInfo().getLocation().y + 1);
        Image image = screenObject.getImage(p1, p2);
        putImage(image);
    }


    protected void putImage(BufferedImage image) {
        getOnRecorded().accept(image);
    }
}
