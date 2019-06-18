package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;
import org.dikhim.jclicker.eventmanager.listener.NonRepeatableSpecifiedKeyPressReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.CreateCodeGenerator;
import org.dikhim.jclicker.util.ShapeUtil;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Recording buffered image
 */
public class AreaRecorder extends StringRecorder implements ScreenRecorder {
    private Point p1;
    CodeGenerator codeGenerator = new CreateCodeGenerator();

    public AreaRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    @Override
    protected void onStart() {
        addListener(
                new NonRepeatableSpecifiedKeyPressReleaseListener(
                        "recording.mouse.controlKey",
                        Dependency.getConfig().hotKeys().mouseControl().getKeys(),
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
        Rectangle r = ShapeUtil.createRectangle(p1, p2);

        putString(codeGenerator.forMethod("rectangle", r.x, r.y, r.width, r.height));
    }


}
