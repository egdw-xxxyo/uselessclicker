package org.dikhim.jclicker.events;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dikhim.jclicker.util.LimitedSizeQueue;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

public class MouseEventsManager implements NativeMouseInputListener {
    private static MouseEventsManager instance;
    private static int x;
    private static int y;
    private Set<String> pressedButtons = new HashSet<>();
	static List<MouseHandler> pressHandlers = new ArrayList<>();
	static List<MouseHandler> releaseHandlers = new ArrayList<>();
	static List<MouseHandler> moveHandlers = new ArrayList<>();
	private static LimitedSizeQueue<String> pressedButtonsLog = new LimitedSizeQueue<>(
			10);
	private static LimitedSizeQueue<String> releasedButtonsLog = new LimitedSizeQueue<>(
			10);
	private static LimitedSizeQueue<Point> movedLog = new LimitedSizeQueue<>(
			10);
	private static LimitedSizeQueue<Long> keysTimeLog = new LimitedSizeQueue<>(2);
	private static LimitedSizeQueue<Long> movedTimeLog = new LimitedSizeQueue<>(2);
	
	//new queues
	private static LimitedSizeQueue<MouseButtonEvent> buttonLog = new LimitedSizeQueue<>(2);
	private static LimitedSizeQueue<MouseMoveEvent> moveLog = new LimitedSizeQueue<>(2);
    private MouseEventsManager() {
    }

    public static MouseEventsManager getInstance() {
        if (instance == null)
            instance = new MouseEventsManager();
        return instance;
    }

    public int getX() {
    	if(!movedLog.isEmpty()) {
    		return movedLog.getLast().x;
    	}else {
    		return 0;
    	}
    }
    public int getXFromEnd(int index) {
    	if(!movedLog.isEmpty()) {
    		return movedLog.getFromEnd(index).x;
    	}else {
    		return 0;
    	}
    }
    
    public int getY() {
    	if(!movedLog.isEmpty()) {
    		return movedLog.getLast().y;
    	}else {
    		return 0;
    	}
    }
    public int getYFromEnd(int index) {
    	if(!movedLog.isEmpty()) {
    		return movedLog.getFromEnd(index).y;
    	}else {
    		return 0;
    	}
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
    	String button = MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton());
    	int x = nativeMouseEvent.getX();
    	int y = nativeMouseEvent.getY();
    	long time = System.currentTimeMillis();
        buttonLog.add(new MouseButtonEvent(button,"PRESS", x, y, time));
    	
		keysTimeLog.add(System.currentTimeMillis());
        pressedButtons.add(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
        pressedButtonsLog.add(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
        for (MouseHandler h : pressHandlers)
			h.fire(pressedButtons);
        
        
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
    	String button = MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton());
    	int x = nativeMouseEvent.getX();
    	int y = nativeMouseEvent.getY();
    	long time = System.currentTimeMillis();
        buttonLog.add(new MouseButtonEvent(button,"RELEASE", x, y, time));
    	
		keysTimeLog.add(System.currentTimeMillis());
        releasedButtonsLog.add(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
        for (MouseHandler h : releaseHandlers)
			h.fire(pressedButtons);
        pressedButtons.remove(MouseCodes.getNameByNativeCode(nativeMouseEvent.getButton()));
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
    	int x = nativeMouseEvent.getX();
    	int y = nativeMouseEvent.getY();
    	long time = System.currentTimeMillis();
    	moveLog.add(new MouseMoveEvent(x, y, time));
    	
		movedTimeLog.add(System.currentTimeMillis());
		movedLog.add(new Point(nativeMouseEvent.getX(),nativeMouseEvent.getY()));
        for (MouseHandler h : moveHandlers)
			h.fire(pressedButtons);
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
    	int x = nativeMouseEvent.getX();
    	int y = nativeMouseEvent.getY();
    	long time = System.currentTimeMillis();
    	moveLog.add(new MouseMoveEvent(x, y, time));
    	
		movedTimeLog.add(System.currentTimeMillis());
		movedLog.add(new Point(nativeMouseEvent.getX(),nativeMouseEvent.getY()));
        for (MouseHandler h : moveHandlers)
			h.fire(pressedButtons);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
    }
    //
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
    
    public void addMoveListener(MouseHandler handler) {
    	MouseHandler existingHandler = null;
		for (MouseHandler h : moveHandlers) {
			if (h.getName().equals(handler.getName()))
				existingHandler = h;
		}
		if (existingHandler != null) {
			existingHandler.setButtons(handler.getButtons());
			existingHandler.setHandler(handler.getHandler());
		} else {
			moveHandlers.add(handler);
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
	 * Remove all listeners with same prefix
	 * 
	 * @param prefix
	 */
	public void removeMoveListenersByPrefix(String prefix) {
		Iterator<MouseHandler> it = moveHandlers.iterator();
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
	/**
	 * Return delay between last two key events
	 * @return
	 */
	public int getLastKeyDelay() {
		if(keysTimeLog.size()>1) {
			return (int) (keysTimeLog.getFromEnd(0) - keysTimeLog.getFromEnd(1));
		}else {
			return 0;
		}
	}
	/**
	 * Return delay between last two movement delays
	 * @return
	 */
	public int getLastMoveDelay() {
		if(movedTimeLog.size()>1) {
			return (int) (movedTimeLog.getFromEnd(0) - movedTimeLog.getFromEnd(1));
		}else {
			return 0;
		}
	}
	/**
	 * Resets the time log for keys events
	 */
	public void resetKeyTimeLog() {
		keysTimeLog.clear();
	}
	
	/**
	 * Resets the time log for movement events
	 */
	public void resetMoveTimeLog() {
		keysTimeLog.clear();
	}
	
	/////////////////////
	public MouseButtonEvent getLastButtonEvent() {
		if(buttonLog.size()>0) {
			return buttonLog.getFromEnd(0);
		}else return null;
	}
	
	public MouseButtonEvent getPreLastButtonEvent() {
		if(buttonLog.size()>1) {
			return buttonLog.getFromEnd(1);
		}else return null;
	}
	
	
	public MouseMoveEvent getLastMoveEvent() {
		if(moveLog.size()>0) {
			return moveLog.getFromEnd(0);
		}else return null;
	}
	
	public MouseMoveEvent getPreLastMoveEvent() {
		if(moveLog.size()>1) {
			return moveLog.getFromEnd(1);
		}else return null;
	}
	
	public void clearButtonEventLog() {
		buttonLog.clear();
	}
	
}
