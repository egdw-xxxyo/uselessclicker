package org.dikhim.jclicker.events;

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

    // logs
    private static LimitedSizeQueue<MouseButtonEvent> buttonLog = new LimitedSizeQueue<>(
            2);
    private static LimitedSizeQueue<MouseWheelEvent> wheelLog = new LimitedSizeQueue<>(
            2);
    private static LimitedSizeQueue<MouseMoveEvent> moveLog = new LimitedSizeQueue<>(
            2);

    private MouseEventsManager() {
    }

    public static MouseEventsManager getInstance() {
        if (instance == null)
            instance = new MouseEventsManager();
        return instance;
    }

    public int getX() {
        return MouseInfo.getPointerInfo().getLocation().x;
    }


    public int getY() {
        return MouseInfo.getPointerInfo().getLocation().y;
    }


    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        // build new button event
        String button = MouseCodes
                .getNameByNativeCode(nativeMouseEvent.getButton());
        if (button.isEmpty()) return;// return if button is unknown
        // add to pressed buttons
        pressedButtons.add(button);

        int x = nativeMouseEvent.getX();
        int y = nativeMouseEvent.getY();
        long time = System.currentTimeMillis();
        MouseButtonEvent buttonEvent = new MouseButtonEvent(button, pressedButtons, "PRESS", x, y, time);

        // add to log
        buttonLog.add(buttonEvent);

        // fire
        for (MouseButtonHandler h : buttonHandlers) {
            h.fire(buttonEvent);
        }

    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        // build new button event
        String button = MouseCodes
                .getNameByNativeCode(nativeMouseEvent.getButton());
        if (button.isEmpty()) return;// return if button is unknown
        // remove from pressed buttons
        pressedButtons.remove(button);

        int x = nativeMouseEvent.getX();
        int y = nativeMouseEvent.getY();
        long time = System.currentTimeMillis();
        MouseButtonEvent buttonEvent = new MouseButtonEvent(button, pressedButtons, "RELEASE", x, y, time);

        // add to log
        buttonLog.add(buttonEvent);

        // fire
        for (MouseButtonHandler h : buttonHandlers)
            h.fire(buttonEvent);
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        // build new move event
        int x = nativeMouseEvent.getX();
        int y = nativeMouseEvent.getY();
        long time = System.currentTimeMillis();
        MouseMoveEvent moveEvent = new MouseMoveEvent(x, y, time);

        // add to log
        moveLog.add(moveEvent);

        // fire
        for (MouseMoveHandler h : moveHandlers)
            h.fire(moveEvent);
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        nativeMouseMoved(nativeMouseEvent);
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseEvent) {
        // build new wheel event
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

        //add to log
        wheelLog.add(wheelEvent);

        //fire
        for (MouseWheelHandler h : wheelHandlers)
            h.fire(wheelEvent);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    }

    /////////////////

    /**
     * Adds mouse buttons listener
     *
     * @param handler
     */
    public void addButtonListener(MouseButtonHandler handler) {
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

    /**
     * Adds mouse move listener
     *
     * @param handler mouse move handler
     */
    public void addMoveListener(MouseMoveHandler handler) {
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

    /**
     * Adds mouse wheel listener
     *
     * @param handler mouse wheel listener
     */
    public void addWheelListener(MouseWheelHandler handler) {
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

    /**
     * Remove all button listeners with same prefix
     *
     * @param prefix
     */

    public void removeButtonListenersByPrefix(String prefix) {
        Iterator<MouseButtonHandler> it = buttonHandlers.iterator();
        while (it.hasNext()) {
            if (it.next().getName().startsWith(prefix))
                it.remove();
        }
    }

    /**
     * Remove all wheel listeners with same prefix
     *
     * @param prefix
     */
    public void removeWheelListenersByPrefix(String prefix) {
        Iterator<MouseWheelHandler> it = wheelHandlers.iterator();
        while (it.hasNext()) {
            if (it.next().getName().startsWith(prefix))
                it.remove();
        }
    }

    /**
     * Remove all move listeners with same prefix
     *
     * @param prefix
     */
    public void removeMoveListenersByPrefix(String prefix) {
        Iterator<MouseMoveHandler> it = moveHandlers.iterator();
        while (it.hasNext()) {
            if (it.next().getName().startsWith(prefix))
                it.remove();
        }
    }

    public void removeListenersByPrefix(String prefix) {
        removeButtonListenersByPrefix(prefix);
        removeWheelListenersByPrefix(prefix);
        removeMoveListenersByPrefix(prefix);
    }


    /////////////////////
    public MouseButtonEvent getLastButtonEvent() {
        if (buttonLog.size() > 0) {
            return buttonLog.getFromEnd(0);
        } else
            return null;
    }

    public MouseButtonEvent getPreLastButtonEvent() {
        if (buttonLog.size() > 1) {
            return buttonLog.getFromEnd(1);
        } else
            return null;
    }

    public MouseWheelEvent getLastWheelEvent() {
        if (wheelLog.size() > 0) {
            return wheelLog.getFromEnd(0);
        } else
            return null;
    }

    public MouseWheelEvent getPreLastWheelEvent() {
        if (wheelLog.size() > 1) {
            return wheelLog.getFromEnd(1);
        } else
            return null;
    }

    public MouseMoveEvent getLastMoveEvent() {
        if (moveLog.size() > 0) {
            return moveLog.getFromEnd(0);
        } else
            return null;
    }

    public MouseMoveEvent getPreLastMoveEvent() {
        if (moveLog.size() > 1) {
            return moveLog.getFromEnd(1);
        } else
            return null;
    }

    public void clearButtonEventLog() {
        buttonLog.clear();
    }

    public void clearMoveEventLog() {
        moveLog.clear();
    }

    public void clearWheelEventLog() {
        wheelLog.clear();
    }

}
