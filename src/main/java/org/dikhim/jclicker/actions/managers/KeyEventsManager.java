package org.dikhim.jclicker.actions.managers;


import java.util.*;

import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.events.KeyboardEvent;
import org.dikhim.jclicker.actions.KeyboardListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyEventsManager implements NativeKeyListener {

    private static KeyEventsManager instance;
    // pressed keys
    static Set<String> pressedKeys = new HashSet<>();

    // handlers
    private static List<KeyboardListener> keyboardListeners = Collections.synchronizedList(new ArrayList<>());

    private KeyEventsManager() {
    }

    public static KeyEventsManager getInstance() {
        if (instance == null)
            instance = new KeyEventsManager();
        return instance;
    }

    @Override
    public synchronized void nativeKeyPressed(NativeKeyEvent e) {
        // build new key events
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        // add to pressed buttons
        pressedKeys.add(key);
        long time = System.currentTimeMillis();
        KeyboardEvent keyboardEvent = new KeyboardEvent(key, pressedKeys, "PRESS", time);

        // fire
        for (KeyboardListener h : keyboardListeners)
            h.fire(keyboardEvent);
    }

    @Override
    public synchronized void nativeKeyReleased(NativeKeyEvent e) {

        // build new key events
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        long time = System.currentTimeMillis();
        KeyboardEvent keyboardEvent = new KeyboardEvent(key, pressedKeys, "RELEASE", time);

        // fire
        for (KeyboardListener h : keyboardListeners)
            h.fire(keyboardEvent);

        // add to pressed buttons
        pressedKeys.remove(key);

    }

    @Override
    public synchronized void nativeKeyTyped(NativeKeyEvent e) {
    }


    public synchronized void addKeyboardListener(KeyboardListener keyboardListener) {
        Thread thread = new Thread(()->{
            synchronized (this) {
                KeyboardListener existingListener = null;
                for (KeyboardListener h : keyboardListeners) {
                    if (h.getName().equals(keyboardListener.getName()))
                        existingListener = h;
                }
                if (existingListener != null) {
                    existingListener.setKeys(keyboardListener.getKeys());
                    existingListener.setHandler(keyboardListener.getHandler());
                } else {
                    keyboardListeners.add(keyboardListener);
                }
            }
        });
        thread.start();
    }

    public synchronized  void removeListenersByPrefix(String prefix) {
        Thread thread = new Thread(()-> {
            synchronized (this) {
                keyboardListeners.removeIf(keyboardListener -> keyboardListener.getName().startsWith(prefix));
            }
        });
        thread.start();
    }

    public synchronized boolean isListenerExist(String name){
        for (KeyboardListener h : keyboardListeners) {
            if (h.getName().equals(name))
                return true;
        }
        return false;
    }
    public synchronized boolean isListenerExistByPrefix(String prefix){
        for (KeyboardListener h : keyboardListeners) {
            if (h.getName().startsWith(prefix))
                return true;
        }
        return false;
    }

    public synchronized boolean isPressed(String key) {
        return pressedKeys.contains(key);
    }

    public synchronized boolean isPressed(Set<String> keys) {
        return pressedKeys.containsAll(keys);
    }

}
