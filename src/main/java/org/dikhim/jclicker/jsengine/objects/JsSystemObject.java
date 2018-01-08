package org.dikhim.jclicker.jsengine.objects;

import org.dikhim.jclicker.actions.managers.KeyEventsManager;
import org.dikhim.jclicker.actions.ShortcutEqualsListener;
import org.dikhim.jclicker.actions.utils.decoders.ActionDecoder;
import org.dikhim.jclicker.actions.utils.decoders.UnicodeDecoder;
import org.dikhim.jclicker.jsengine.JSEngine;
import org.dikhim.jclicker.util.output.Out;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

@SuppressWarnings("unused")
public class JsSystemObject implements SystemObject {
    private JSEngine engine;
    private Robot robot;

    public JsSystemObject(JSEngine engine) {
        this.engine = engine;
        this.robot = engine.getRobot();
    }

    public JsSystemObject(Robot robot) {
        this.robot = robot;
    }

    public void print(String s) {
        Out.print(s);
    }

    public void println(String s) {
        Out.println(s);
    }

    /**
     * Register shortcut for call function
     *
     * @param function name of function
     * @param shortcut list of names of keys
     */

    public void registerShortcut(String shortcut, String function) {
        ShortcutEqualsListener handler = new ShortcutEqualsListener("script." + function,
                shortcut, "PRESS", (e) -> {
            engine.addTask(() -> {
                engine.invokeFunction(function);
            });
        });
        KeyEventsManager.getInstance().addKeyboardListener(handler);
    }

    public void sleep(int ms) {
        if(ms<=0)return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }


}
