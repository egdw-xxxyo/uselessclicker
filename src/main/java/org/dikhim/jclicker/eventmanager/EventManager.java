package org.dikhim.jclicker.eventmanager;

import org.dikhim.jclicker.eventmanager.event.*;
import org.dikhim.jclicker.eventmanager.event.Event;
import org.dikhim.jclicker.eventmanager.filter.Filter;
import org.dikhim.jclicker.eventmanager.listener.*;
import org.dikhim.jclicker.eventmanager.listener.EventListener;
import org.dikhim.jclicker.eventmanager.utils.KeyCodes;
import org.dikhim.jclicker.eventmanager.utils.MouseCodes;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.*;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EventManager implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener, NativeMouseWheelListener {

    private Map<String, EventListener> listeners = new LinkedHashMap<>();

    public EventManager(){
        PrintStream oldOut = System.out;
        
        try {
            Logger logger = Logger
                    .getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            }));
            
            
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.addNativeMouseMotionListener(this);
            GlobalScreen.addNativeMouseWheelListener(this);
        } catch (NativeHookException ignored) {
        }finally {
            System.setOut(oldOut);
        }
        
    }

    // KEY
    synchronized public void nativeKeyPressed(NativeKeyEvent e) {
        // build new key event
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        long time = System.currentTimeMillis();
        KeyPressEvent keyPressEvent = new KeyPressEvent(key, time);

        // fire
        fire(keyPressEvent);
    }

    synchronized  public void nativeKeyReleased(NativeKeyEvent e) {
        // build new key event
        String key = KeyCodes.getNameByNativeCode(e.getKeyCode());
        if (key.isEmpty()) return;
        long time = System.currentTimeMillis();
        KeyReleaseEvent keyReleaseEvent = new KeyReleaseEvent(key, time);

        // fire
        fire(keyReleaseEvent);
    }

    synchronized public void nativeKeyTyped(NativeKeyEvent e) {
        // ignored
    }

    // MOUSE BUTTONS
    synchronized public void nativeMousePressed(NativeMouseEvent e) {
        // build new event
        String button = MouseCodes.getNameByNativeCode(e.getButton());
        if (button.isEmpty()) return;

        int x = e.getX();
        int y = e.getY();
        long time = System.currentTimeMillis();
        MousePressEvent mousePressEvent = new MousePressEvent(button, x, y, time);

        // buttonReleased
        fire(mousePressEvent);
    }

    synchronized public void nativeMouseReleased(NativeMouseEvent e) {
        // build new event
        String button = MouseCodes.getNameByNativeCode(e.getButton());
        if (button.isEmpty()) return;
        int x = e.getX();
        int y = e.getY();
        long time = System.currentTimeMillis();
        MouseReleaseEvent mouseReleaseEvent = new MouseReleaseEvent(button, x, y, time);

        // buttonReleased
        fire(mouseReleaseEvent);
    }

    synchronized public void nativeMouseClicked(NativeMouseEvent e) {
        // ignored
    }

    // MOUSE MOVEMENT
    synchronized public void nativeMouseMoved(NativeMouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        long time = System.currentTimeMillis();
        MouseMoveEvent mouseMoveEvent = new MouseMoveEvent(x, y, time);

        fire(mouseMoveEvent);
    }

    synchronized public void nativeMouseDragged(NativeMouseEvent e) {
        nativeMouseMoved(e);
    }

    // MOUSE WHEEL
    synchronized public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        // build new wheel events
        int x = e.getX();
        int y = e.getY();
        int amount = e.getScrollAmount();
        long time = System.currentTimeMillis();

        Event wheelEvent;
        if (e.getWheelRotation() < 0) {
            wheelEvent = new MouseWheelUpEvent(amount, x, y, time);
        } else {
            wheelEvent = new MouseWheelDownEvent(amount, x, y, time);
        }

        fire(wheelEvent);
    }


    /////////////////////// FIRE
    private void fire(Event e) {
        if (e instanceof KeyPressEvent) {
            listeners.forEach((key, listener) -> {
                if (ignored(key, e, listener)) return;
                if (listener instanceof KeyPressListener) {
                    ((KeyPressListener) listener).keyPressed((KeyPressEvent) e);
                }
            });
        } else if (e instanceof KeyReleaseEvent) {
            KeyReleaseEvent event = (KeyReleaseEvent) e;
            listeners.forEach((key, listener) -> {
                if (ignored(key, e, listener)) return;
                if (listener instanceof KeyReleaseListener) {
                    ((KeyReleaseListener) listener).keyReleased(event);
                }
            });
        } else if (e instanceof MousePressEvent) {
            MousePressEvent event = (MousePressEvent) e;
            listeners.forEach((key, listener) -> {
                if (ignored(key, e, listener)) return;
                if (listener instanceof MousePressListener) {
                    ((MousePressListener) listener).buttonPressed(event);
                }
            });
        } else if (e instanceof MouseReleaseEvent) {
            MouseReleaseEvent event = (MouseReleaseEvent) e;
            listeners.forEach((key, listener) -> {
                if (ignored(key, e, listener)) return;
                if (listener instanceof MouseReleaseListener) {
                    ((MouseReleaseListener) listener).buttonReleased(event);
                }
            });
        } else if (e instanceof MouseMoveEvent) {
            MouseMoveEvent event = (MouseMoveEvent) e;
            listeners.forEach((key, listener) -> {
                if (ignored(key, e, listener)) return;
                if (listener instanceof MouseMoveListener) {
                    ((MouseMoveListener) listener).mouseMoved(event);
                }
            });
        } else if (e instanceof MouseWheelUpEvent) {
            MouseWheelUpEvent event = (MouseWheelUpEvent) e;
            listeners.forEach((key, listener) -> {
                if (ignored(key, e, listener)) return;
                if (listener instanceof MouseWheelUpListener) {
                    ((MouseWheelUpListener) listener).wheeledUp(event);
                }
            });
        } else if (e instanceof MouseWheelDownEvent) {
            MouseWheelDownEvent event = (MouseWheelDownEvent) e;
            listeners.forEach((key, listener) -> {
                if (ignored(key, e, listener)) return;
                if (listener instanceof MouseWheelDownListener) {
                    ((MouseWheelDownListener) listener).wheeledDown(event);
                }
            });
        }
    }

    private boolean ignored(String key, Event event, EventListener listener) {
        for (Filter filter : filters) {
            if (filter.ignored(key, event, listener)) return true;
        }
        return false;
    }

    synchronized public int getX() {
        return MouseInfo.getPointerInfo().getLocation().x;
    }


    synchronized public int getY() {
        return MouseInfo.getPointerInfo().getLocation().y;
    }


    /////////////////////// PUBLIC
    synchronized public void addListener(String key, EventListener listener) {
        listeners.put(key, listener);
    }

    synchronized public void removeListener(String key) {
        listeners.remove(key);
    }

    synchronized public void removeListener(EventListener listener) {
        while (listeners.values().remove(listener)) ;
    }


    private List<Filter> filters = new ArrayList<>();

    synchronized  public void addFilter(Filter filter) {
        filters.add(filter);
    }

    synchronized public void removeFilter(Filter filter) {
        filters.remove(filter);
    }
}