package org.dikhim.jclicker.actions.managers;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

import org.dikhim.jclicker.actions.utils.KeyCodes;
import org.dikhim.jclicker.actions.events.KeyboardEvent;
import org.dikhim.jclicker.actions.KeyboardListener;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyEventsManager implements NativeKeyListener{

    private static KeyEventsManager instance;
    // press keys
    private Set<String> pressedKeys = new HashSet<>();
    private List<String> ignoredPrefixes = new ArrayList<>();

    // handlers
    private static List<KeyboardListener> keyboardListeners = Collections.synchronizedList(new ArrayList<>());

    private KeyEventsManager() {
    }

    public static KeyEventsManager getInstance() {
        if (instance == null)
            instance = new KeyEventsManager();
        return instance;
    }

    private void fire(KeyboardEvent keyboardEvent) {
        for (KeyboardListener listener : keyboardListeners) {
            boolean ignored = false;
            for (String prefix : ignoredPrefixes) {
                if (listener.getName().startsWith(prefix)) {
                    ignored = true;
                    break;
                }
            }
            if (!ignored) listener.fire(keyboardEvent);
        }

    }

    @Override
    public synchronized void nativeKeyPressed(NativeKeyEvent e) {
        // build new key events
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        // add to press buttons
        pressedKeys.add(key);
        long time = System.currentTimeMillis();
        KeyboardEvent keyboardEvent = new KeyboardEvent(key, pressedKeys, "PRESS", time);

        // fire
        fire(keyboardEvent);
    }

    @Override
    public synchronized void nativeKeyReleased(NativeKeyEvent e) {

        // build new key events
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        long time = System.currentTimeMillis();
        KeyboardEvent keyboardEvent = new KeyboardEvent(key, pressedKeys, "RELEASE", time);

        // fire
        fire(keyboardEvent);

        // add to press buttons
        pressedKeys.remove(key);

    }

    @Override
    public synchronized void nativeKeyTyped(NativeKeyEvent e) {
    }


    public synchronized void addKeyboardListener(KeyboardListener keyboardListener) {
        Thread thread = new Thread(() -> {
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

    public synchronized void removeListenersByPrefix(String prefix) {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                keyboardListeners.removeIf(keyboardListener -> keyboardListener.getName().startsWith(prefix));
            }
        });
        thread.start();
    }

    public synchronized boolean isListenerExist(String name) {
        for (KeyboardListener h : keyboardListeners) {
            if (h.getName().equals(name))
                return true;
        }
        return false;
    }

    public synchronized boolean isListenerExistByPrefix(String prefix) {
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

    public synchronized void ignorePrefix(String prefix) {
        ignoredPrefixes.add(prefix);
    }

    public synchronized void removeIgnorePrefix(String prefix) {
        ignoredPrefixes.remove(prefix);
    }
    
    public synchronized boolean isCapsLockLocked(){
        return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
    }
    public synchronized boolean isNumLockLocked(){
        return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
    }
    public synchronized boolean isScrollLockLocked(){
        return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
    }
}
