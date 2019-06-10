package org.dikhim.jclicker.jsengine.clickauto.objects;

import javafx.application.Platform;
import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.objects.Classes.Image;
import org.dikhim.clickauto.jsengine.objects.ScriptSystemObject;
import org.dikhim.jclicker.Clicker;
import org.dikhim.jclicker.Dependency;
import org.dikhim.jclicker.eventmanager.event.MouseMoveEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelDownEvent;
import org.dikhim.jclicker.eventmanager.event.MouseWheelUpEvent;
import org.dikhim.jclicker.eventmanager.filter.KeyboardFilter;
import org.dikhim.jclicker.eventmanager.filter.MousePrefixFilter;
import org.dikhim.jclicker.eventmanager.listener.*;

public class UselessSystemObject extends ScriptSystemObject implements SystemObject {
    public UselessSystemObject(ClickAutoScriptEngine engine) {
        super(engine);
    }

    @Override
    public void exit() {
        Platform.exit();
    }


    @Override
    public void keyIgnore() {
        Dependency.getEventManager().addFilter(new KeyboardFilter("script.keyboard.ignore"));
    }

    @Override
    public void keyResume() {
        Dependency.getEventManager().removeFiltersWithPrefix("script.keyboard");
    }

    @Override
    public void mouseIgnore() {
        Dependency.getEventManager().addFilter(new MousePrefixFilter("script.mouse.ignore", "script."));
    }

    @Override
    public void mouseResume() {
        Dependency.getEventManager().removeFiltersWithPrefix("script.mouse");
    }

    @Override
    public void onKeyPress(String functionName, String key, Object... args) {
        String id = "script." + functionName + "." + key + ".press";
        Dependency.getEventManager().addListener(new SpecifiedKeyPressListener(id, key, () -> engine.invokeFunction(functionName, args)));
    }

    @Override
    public void onKeyRelease(String functionName, String key, Object... args) {
        String id = "script." + functionName + "." + key + ".release";
        Dependency.getEventManager().addListener(new SpecifiedKeyReleaseListener(id, key, () -> engine.invokeFunction(functionName, args)));
    }

    @Override
    public void onShortcutPress(String functionName, String keys, Object... args) {
        String id = "script." + functionName + "." + keys + ".press";
        Dependency.getEventManager().addListener(new ShortcutPressListener(id, keys, () -> engine.invokeFunction(functionName, args)));
    }

    @Override
    public void onShortcutRelease(String functionName, String keys, Object... args) {
        String id = "script." + functionName + "." + keys + ".release";
        Dependency.getEventManager().addListener(new ShortcutReleaseListener(id, keys, () -> engine.invokeFunction(functionName, args)));
    }

    @Override
    public void onMousePress(String functionName, String button, Object... args) {
        String id = "script." + functionName + "." + button + ".press";
        Dependency.getEventManager().addListener(new SpecifiedMouseButtonPressListener(id, button, () -> engine.invokeFunction(functionName, args)));
    }

    @Override
    public void onMouseRelease(String functionName, String button, Object... args) {
        String id = "script." + functionName + "." + button + ".release";
        Dependency.getEventManager().addListener(new SpecifiedMouseButtonPressListener(id, button, () -> engine.invokeFunction(functionName, args)));
    }

    @Override
    public void onMouseMove(String functionName, Object... args) {
        String id = "script." + functionName + ".move";
        Dependency.getEventManager().addListener(new SimleMouseMoveListener(id) {
            @Override
            public void mouseMoved(MouseMoveEvent event) {
                engine.invokeFunction(functionName, args);
            }
        });
    }

    @Override
    public void onWheelDown(String functionName, Object... args) {
        String id = "script." + functionName + ".wheel.down";
        Dependency.getEventManager().addListener(new SimpleMouseWheelListener(id) {
            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
                engine.invokeFunction(functionName, args);
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {

            }
        });
    }

    @Override
    public void onWheelUp(String functionName, Object... args) {
        String id = "script." + functionName + ".wheel.up";
        Dependency.getEventManager().addListener(new SimpleMouseWheelListener(id) {
            @Override
            public void wheeledDown(MouseWheelDownEvent event) {
            }

            @Override
            public void wheeledUp(MouseWheelUpEvent event) {
                engine.invokeFunction(functionName, args);
            }
        });
    }

    @Override
    public void setMaxThreads(String name, int maxThreads) {
        engine.registerInvocableMethod(name, maxThreads);
    }

    @Override
    public void showImage(Image image) {
        synchronized (robot) {
            Platform.runLater(() -> Clicker.getApplication().getMainApplication().getOnSetOutputImage().accept(image));
        }
    }
}
