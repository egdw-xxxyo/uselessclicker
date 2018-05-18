package org.dikhim.jclicker.actions.managers;

import org.dikhim.jclicker.actions.*;
import org.dikhim.jclicker.actions.events.MouseButtonEvent;
import org.dikhim.jclicker.actions.events.MouseMoveEvent;
import org.dikhim.jclicker.actions.events.MouseWheelEvent;
import org.dikhim.jclicker.actions.utils.MouseCodes;
import org.dikhim.jclicker.util.LimitedSizeQueue;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MouseEventsManager
        implements
        NativeMouseInputListener,
        NativeMouseWheelListener {
    private static MouseEventsManager instance;

    // pressed buttons
    private Set<String> pressedButtons = new HashSet<>();

    // handlers
    private static List<MouseButtonHandler> buttonHandlers = Collections.synchronizedList(new ArrayList<>());
    private static List<MouseWheelHandler> wheelHandlers = Collections.synchronizedList(new ArrayList<>());
    private static List<MouseMoveHandler> moveHandlers = Collections.synchronizedList(new ArrayList<>());

    private MouseEventsManager() {
    }

    public static MouseEventsManager getInstance() {
        if (instance == null)
            instance = new MouseEventsManager();
        return instance;
    }

    synchronized public int getX() {
        return MouseInfo.getPointerInfo().getLocation().x;
    }


    synchronized public int getY() {
        return MouseInfo.getPointerInfo().getLocation().y;
    }


    @Override
    public synchronized void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        // build new button events
        String button = MouseCodes
                .getNameByNativeCode(nativeMouseEvent.getButton());
        if (button.isEmpty()) return;// return if button is unknown
        // add to pressed buttons
        pressedButtons.add(button);

        int x = nativeMouseEvent.getX();
        int y = nativeMouseEvent.getY();
        long time = System.currentTimeMillis();
        MouseButtonEvent buttonEvent = new MouseButtonEvent(button, pressedButtons, "PRESS", x, y, time);

        // fire
        for (MouseButtonHandler h : buttonHandlers) {
            h.fire(buttonEvent);
        }

    }

    @Override
    public synchronized void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        // build new button events
        String button = MouseCodes
                .getNameByNativeCode(nativeMouseEvent.getButton());
        if (button.isEmpty()) return;// return if button is unknown
        // remove from pressed buttons
        pressedButtons.remove(button);

        int x = nativeMouseEvent.getX();
        int y = nativeMouseEvent.getY();
        long time = System.currentTimeMillis();
        MouseButtonEvent buttonEvent = new MouseButtonEvent(button, pressedButtons, "RELEASE", x, y, time);

        // fire
        for (MouseButtonHandler h : buttonHandlers)
            h.fire(buttonEvent);
    }

    @Override
    public synchronized void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        // build new move events
        int x = nativeMouseEvent.getX();
        int y = nativeMouseEvent.getY();
        long time = System.currentTimeMillis();
        MouseMoveEvent moveEvent = new MouseMoveEvent(x, y, time);

        // fire
        for (MouseMoveHandler h : moveHandlers)
            h.fire(moveEvent);
    }

    @Override
    public synchronized void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        nativeMouseMoved(nativeMouseEvent);
    }

    @Override
    public synchronized void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseEvent) {
        // build new wheel events
        int x = nativeMouseEvent.getX();
        int y = nativeMouseEvent.getY();
        int amount = nativeMouseEvent.getScrollAmount();
        long time = System.currentTimeMillis();
        String direction;
        if (nativeMouseEvent.getWheelRotation() < 0) {
            direction = "UP";
        } else {
            direction = "DOWN";
        }
        MouseWheelEvent wheelEvent = new MouseWheelEvent(direction, amount, time, x, y);

        //fire
        for (MouseWheelHandler h : wheelHandlers)
            h.fire(wheelEvent);
    }

    @Override
    public synchronized void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    }

    /////////////////

    /**
     * Adds mouse buttons listener
     *
     * @param handler mouseHandler
     */
    public synchronized void addButtonListener(MouseButtonHandler handler) {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                MouseButtonHandler existingHandler = null;
                for (MouseButtonHandler h : buttonHandlers) {
                    if (h.getName().equals(handler.getName()))
                        existingHandler = h;
                }
                if (existingHandler != null) {
                    existingHandler.setButtons(handler.getButtons());
                    existingHandler.setHandler(handler.getHandler());
                } else {
                    buttonHandlers.add(handler);
                }
            }
        });
        thread.start();
    }

    /**
     * Adds mouse move listener
     *
     * @param handler mouse move handler
     */
    public synchronized void addMoveListener(MouseMoveHandler handler) {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                MouseMoveHandler existingHandler = null;
                for (MouseMoveHandler h : moveHandlers) {
                    if (h.getName().equals(handler.getName()))
                        existingHandler = h;
                }
                if (existingHandler != null) {
                    existingHandler.setHandler(handler.getHandler());
                } else {
                    moveHandlers.add(handler);
                }
            }
        });
        thread.start();
    }

    /**
     * Adds mouse wheel listener
     *
     * @param handler mouse wheel listener
     */
    public synchronized void addWheelListener(MouseWheelHandler handler) {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                MouseWheelHandler existingHandler = null;
                for (MouseWheelHandler h : wheelHandlers) {
                    if (h.getName().equals(handler.getName()))
                        existingHandler = h;
                }
                if (existingHandler != null) {
                    existingHandler.setDirection(handler.getDirection());
                    existingHandler.setHandler(handler.getHandler());
                } else {
                    wheelHandlers.add(handler);
                }
            }
        });
        thread.start();
    }

    /**
     * Remove all button listeners with same prefix
     *
     * @param prefix prefix of listeners
     */

    private synchronized void removeButtonListenersByPrefix(String prefix) {
                buttonHandlers.removeIf(mouseButtonHandler -> mouseButtonHandler.getName().startsWith(prefix));
    }

    /**
     * Remove all wheel listeners with same prefix
     *
     * @param prefix prefix of listeners
     */
    private synchronized void removeWheelListenersByPrefix(String prefix) {
                wheelHandlers.removeIf(mouseWheelHandler -> mouseWheelHandler.getName().startsWith(prefix));
    }

    /**
     * Remove all move listeners with same prefix
     *
     * @param prefix prefix of listeners
     */
    private synchronized void removeMoveListenersByPrefix(String prefix) {
                moveHandlers.removeIf(mouseMoveHandler -> mouseMoveHandler.getName().startsWith(prefix));
    }
    /**
     * Remove all listeners with same prefix
     *
     * @param prefix prefix of listeners
     */
    public synchronized void removeListenersByPrefix(String prefix) {
        Thread thread = new Thread(()-> {
            synchronized (this) {
                removeButtonListenersByPrefix(prefix);
                removeWheelListenersByPrefix(prefix);
                removeMoveListenersByPrefix(prefix);
            }
        });
        thread.start();
    }
}
