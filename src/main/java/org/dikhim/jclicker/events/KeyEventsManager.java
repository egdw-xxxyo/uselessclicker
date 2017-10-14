package org.dikhim.jclicker.events;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dikhim.jclicker.util.LimitedSizeQueue;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyEventsManager implements NativeKeyListener {

	private static KeyEventsManager instance;
	static Set<String> pressedKeys = new HashSet<>();
	static List<ShortcutHandler> pressHandlers = new ArrayList<>();
	static List<ShortcutHandler> releaseHandlers = new ArrayList<>();
	private static LimitedSizeQueue<String> pressedKeysLog = new LimitedSizeQueue<>(
			2);
	private static LimitedSizeQueue<String> releasedKeysLog = new LimitedSizeQueue<>(
			2);
	private static LimitedSizeQueue<Long> timeLog = new LimitedSizeQueue<>(2);
	private KeyEventsManager() {
	}
	public static KeyEventsManager getInstance() {
		if (instance == null)
			instance = new KeyEventsManager();
		return instance;
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		timeLog.add(System.currentTimeMillis());
		pressedKeys.add(KeyCodes.getNameByNativeCode(e.getKeyCode()));
		pressedKeysLog.add(KeyCodes.getNameByNativeCode(e.getKeyCode()));
		for (ShortcutHandler h : pressHandlers)
			h.fire(pressedKeys);
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		timeLog.add(System.currentTimeMillis());
		releasedKeysLog.add(KeyCodes.getNameByNativeCode(e.getKeyCode()));
		for (ShortcutHandler h : releaseHandlers)
			h.fire(pressedKeys);
		pressedKeys.remove(KeyCodes.getNameByNativeCode(e.getKeyCode()));
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {}
	/**
	 * Adds press handler. Name of the handler must be unique
	 * 
	 * @param handler
	 */
	public void addPressListener(ShortcutHandler handler) {
		ShortcutHandler existingHandler = null;
		for (ShortcutHandler h : pressHandlers) {
			if (h.getName().equals(handler.getName()))
				existingHandler = h;
		}
		if (existingHandler != null) {
			existingHandler.setShortcuts(handler.getShortcuts());
			existingHandler.setHandler(handler.getHandler());
		} else {

			pressHandlers.add(handler);
		}
	}
	
	public void addReleaseListener(ShortcutHandler handler) {
		ShortcutHandler existingHandler = null;
		for (ShortcutHandler h : releaseHandlers) {
			if (h.getName().equals(handler.getName()))
				existingHandler = h;
		}
		if (existingHandler != null) {
			existingHandler.setShortcuts(handler.getShortcuts());
			existingHandler.setHandler(handler.getHandler());
		} else {

			releaseHandlers.add(handler);
		}
	}
	/**
	 * Remove all handlers with same prefix
	 * 
	 * @param prefix
	 */
	public void removePressListenersByPrefix(String prefix) {
		Iterator<ShortcutHandler> it = pressHandlers.iterator();
		while (it.hasNext()) {
			if (it.next().getName().startsWith(prefix))
				it.remove();
		}
	}
	/**
	 * Remove all handlers with same prefix
	 * 
	 * @param prefix
	 */
	public void removeReleaseListenersByPrefix(String prefix) {
		Iterator<ShortcutHandler> it = releaseHandlers.iterator();
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
		return pressedKeysLog.getLast();
	}
	/**
	 * 
	 * @return the name of the last released key
	 */
	public String getLastReleased() {
		return releasedKeysLog.getLast();
	}
	/**
	 * Return true if one of the pressed keys is 'key'
	 * @param key
	 * @return
	 */
	public boolean isPressed(String key) {
		return pressedKeys.contains(key);
	}
	/**
	 * Return delay between last two key events
	 * @return
	 */
	public int getLastDelay() {
		if(timeLog.size()>1) {
			return (int) (timeLog.getFromEnd(0) - timeLog.getFromEnd(1));
		}else {
			return 0;
		}
	}
	/**
	 * Resets the time log fo input events
	 */
	public void resetTimeLog() {
		timeLog.clear();
	}
}
