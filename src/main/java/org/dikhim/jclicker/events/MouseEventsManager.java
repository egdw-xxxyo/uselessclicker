package org.dikhim.jclicker.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dikhim.jclicker.util.LimitedSizeQueue;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

/**
 * Created by dikobraz on 22.03.17.
 */
public class MouseEventsManager implements NativeMouseInputListener {
    private static MouseEventsManager instance;
    private static int x;
    private static int y;
    private Set<String> pressedButtons = new HashSet<>();
	static List<MouseHandler> pressHandlers = new ArrayList<>();
	static List<MouseHandler> releaseHandlers = new ArrayList<>();
	private static LimitedSizeQueue<String> pressedButtonsLog = new LimitedSizeQueue<>(
			10);
	private static LimitedSizeQueue<String> releasedButtonsLog = new LimitedSizeQueue<>(
			10);
	
    private MouseEventsManager() {
    }

    public static MouseEventsManager getInstance() {
        if (instance == null)
            instance = new MouseEventsManager();
        return instance;
    }



    
    public int getX() {
    	return x;
    }
    
    public int getY() {
    	return y;
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        pressedButtons.add(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
        pressedButtonsLog.add(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
        x = nativeMouseEvent.getX();
        y = nativeMouseEvent.getY();
        for (MouseHandler h : pressHandlers)
			h.fire(pressedButtons);
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {

        releasedButtonsLog.add(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
        for (MouseHandler h : releaseHandlers)
			h.fire(pressedButtons);
        pressedButtons.remove(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
        x = nativeMouseEvent.getX();
        y = nativeMouseEvent.getY();
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        x = nativeMouseEvent.getX();
        y = nativeMouseEvent.getY();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        x = nativeMouseEvent.getX();
        y = nativeMouseEvent.getY();
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    }
    
    public void addPressListener(MouseHandler handler) {
    	MouseHandler existingHandler = null;
		for (MouseHandler h : pressHandlers) {
			if (h.getName().equals(handler.getName()))
				existingHandler = h;
		}
		if (existingHandler != null) {
			existingHandler.setButtons(handler.getButtons());
			existingHandler.setHandler(handler.getHandler());
		} else {
			pressHandlers.add(handler);
		}
    }
    public void addReleaseListener(MouseHandler handler) {
    	MouseHandler existingHandler = null;
		for (MouseHandler h : releaseHandlers) {
			if (h.getName().equals(handler.getName()))
				existingHandler = h;
		}
		if (existingHandler != null) {
			existingHandler.setButtons(handler.getButtons());
			existingHandler.setHandler(handler.getHandler());
		} else {
			releaseHandlers.add(handler);
		}
    }
    
	/**
	 * Remove all listeners with same prefix
	 * 
	 * @param prefix
	 */
	public void removePressListenersByPrefix(String prefix) {
		Iterator<MouseHandler> it = pressHandlers.iterator();
		while (it.hasNext()) {
			if (it.next().getName().startsWith(prefix))
				it.remove();
		}
	}
	/**
	 * Remove all listeners with same prefix
	 * 
	 * @param prefix
	 */
	public void removeReleaseListenersByPrefix(String prefix) {
		Iterator<MouseHandler> it = releaseHandlers.iterator();
		while (it.hasNext()) {
			if (it.next().getName().startsWith(prefix))
				it.remove();
		}
	}
	
	/**
	 * 
	 * @return the name of the last pressed key
	 */
	public String getLastPressed() {
		return pressedButtonsLog.getLast();
	}
	
	/**
	 * 
	 * @return the name of the last released key
	 */
	public String getLastReleased() {
		return releasedButtonsLog.getLast();
	}
}
