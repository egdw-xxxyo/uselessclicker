package org.dikhim.clickauto.jsengine.objects;

import org.dikhim.clickauto.jsengine.ClickAutoScriptEngine;
import org.dikhim.clickauto.jsengine.robot.Robot;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains methods to manipulate script objects
 */
public class ObjectContainer {
    private final ClickAutoScriptEngine engine;
    private final Robot robot;

    private final Map<String, Object> defaultObjects = new HashMap<>();
    private final Map<String, Object> objects = new HashMap<>();

    public ObjectContainer(ClickAutoScriptEngine engine) {
        this.engine = engine;
        this.robot = engine.getRobot();

        KeyboardObject keyboardObject = new ScriptKeyboardObject(robot);
        MouseObject mouseObject = new ScriptMouseObject(robot);
        SystemObject systemObject = new ScriptSystemObject(engine);
        CombinedObject combinedObject = new ScriptCombinedObject(mouseObject, keyboardObject, systemObject);
        ClipboardObject clipboardObject = new ScriptClipboardObject(robot);
        ScreenObject screenObject = new ScriptScreenObject(robot);
        CreateObject createObject = new ScriptCreateObject();
        ThreadObject threadObject = new ScriptThreadObject();
        defaultObjects.put("key", keyboardObject);
        defaultObjects.put("mouse", mouseObject);
        defaultObjects.put("system", systemObject);
        defaultObjects.put("combined", combinedObject);
        defaultObjects.put("clipboard", clipboardObject);
        defaultObjects.put("screen", screenObject);
        defaultObjects.put("create", createObject);
        defaultObjects.put("thread", threadObject);
        objects.putAll(defaultObjects);
    }
    /**
     * Puts an object to the engine<br>
     * All public methods in object will be accessible via script "objectName.methodName()"<br>
     * To override default object put a new one with the same name
     *
     * @param name   of object
     * @param object instance
     */
    public void put(String name, Object object) {
        objects.put(name, object);
    }

    /**
     * Returns the specified object by name
     *
     * @param name of object
     * @return script object
     */
    public Object get(String name) {
        return objects.get(name);
    }

    /**
     * Remove object by name
     * @param name object's name
     */
    public void remove(String name) {
        objects.remove(name);
    }

    /**
     * Resets objects to default. Will contains only mouse, key, system, combined, clipboard, create and thread objects
     */
    public void resetToDefault() {
        objects.clear();
        objects.putAll(defaultObjects);
    }

    /**
     * Returns the object's map. Where a key is a name of object and a value is the object itself
     * @return f map of objects
     */
    public Map<String, Object> getObjects() {
        return objects;
    }

    /**
     * Returns a map of default objects. Where a key is a name of object and a value is the object itself
     * @return a map of default objects
     */
    public Map<String, Object> getDefaultObjects() {
        return defaultObjects;
    }
}
