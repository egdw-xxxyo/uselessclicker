package org.dikhim.jclicker.events;


import java.util.*;

import org.dikhim.jclicker.util.LimitedSizeQueue;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyEventsManager implements NativeKeyListener {

    private static KeyEventsManager instance;
    // pressed keys
    static Set<String> pressedKeys = new HashSet<>();

    // handlers
    private static List<KeyboardListener> keyboardListeners = Collections.synchronizedList(new ArrayList<>());

    // logs
    private static LimitedSizeQueue<KeyboardEvent> keyLog = new LimitedSizeQueue<>(
            2);

    private KeyEventsManager() {
    }

    public static KeyEventsManager getInstance() {
        if (instance == null)
            instance = new KeyEventsManager();
        return instance;
    }

    @Override
    public synchronized void nativeKeyPressed(NativeKeyEvent e) {
        // build new key event
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        // add to pressed buttons
        pressedKeys.add(key);
        long time = System.currentTimeMillis();
        KeyboardEvent keyboardEvent = new KeyboardEvent(key, pressedKeys, "PRESS", time);

        // add to log
        keyLog.add(keyboardEvent);

        // fire
        for (KeyboardListener h : keyboardListeners)
            h.fire(keyboardEvent);
    }

    @Override
    public synchronized void nativeKeyReleased(NativeKeyEvent e) {

        // build new key event
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        long time = System.currentTimeMillis();
        KeyboardEvent keyboardEvent = new KeyboardEvent(key, pressedKeys, "RELEASE", time);

        // add to log
        keyLog.add(keyboardEvent);

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
                    existingListener.setShortcuts(keyboardListener.getShortcuts());
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
                Iterator<KeyboardListener> it = keyboardListeners.iterator();
                while (it.hasNext()) {
                    if (it.next().getName().startsWith(prefix))
                        it.remove();
                }
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

    public synchronized void clearLog() {
        keyLog.clear();
    }
    public synchronized boolean isPressed(String key) {
        return pressedKeys.contains(key);
    }

    public synchronized boolean isPressed(Set<String> keys) {
        return pressedKeys.containsAll(keys);
    }

}
