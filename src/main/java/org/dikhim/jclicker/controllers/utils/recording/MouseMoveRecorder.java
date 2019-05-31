package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.eventmanager.listener.KeyPressReleaseListener;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseObjectCodeGenerator;

import java.awt.*;
import java.util.function.Consumer;

public class MouseMoveRecorder extends SimpleRecorder implements MouseRecorder {
    public MouseMoveRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private Point point1;

    @Override
    public void onStart() {
        MouseObjectCodeGenerator mouseObjectCodeGenerator = new MouseObjectCodeGenerator();
        addListener("recording.mouse.controlKey", new KeyPressReleaseListener("CONTROL",
                () -> point1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y),
                () -> {
                    System.out.println(point1);
                    Point point2 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);

                    mouseObjectCodeGenerator.move(point2.x - point1.y, point2.y - point1.y);
                    putCode(mouseObjectCodeGenerator.getGeneratedCode());
                }));

    }
}
