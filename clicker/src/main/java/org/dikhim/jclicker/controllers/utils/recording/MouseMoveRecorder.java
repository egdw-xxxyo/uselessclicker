package org.dikhim.jclicker.controllers.utils.recording;

import org.dikhim.jclicker.jsengine.clickauto.generators.CodeGenerator;
import org.dikhim.jclicker.jsengine.clickauto.generators.MouseCodeGenerator;

import java.awt.*;
import java.util.function.Consumer;

/**
 * mouse.move(153,-1);
 */
public class MouseMoveRecorder extends SimpleMouseRecorder implements LupeRequired {
    public MouseMoveRecorder(Consumer<String> onRecorded) {
        super(onRecorded);
    }

    private Point point1;

    CodeGenerator codeGenerator = new MouseCodeGenerator();
    
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onRecordingStarted() {
        point1 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
    }

    @Override
    protected void onRecordingStopped() {
        Point point2 = new Point(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
        putString(codeGenerator.forMethod("move", point2.x - point1.x, point2.y - point1.y));
    }
}
