package org.dikhim.jclicker.events;

import java.util.HashSet;
import java.util.Set;

public class ShortcutEqualsHandler implements ShortcutHandler {
	private String name;
	private Set<Shortcut> shortcuts = new HashSet<>();
	private Runnable handler;
	public ShortcutEqualsHandler(String name, String shortcut, Runnable handler) {
		this.name = name;
		shortcuts.add(new Shortcut(shortcut));
		this.handler = handler;
	}
	
	public void addShortcut(String shortcut) {
		shortcuts.add(new Shortcut(shortcut));
	}
	/**
	 * Runs if all keys in shortcut eequal to pressed keys
	 * @param keys
	 */
	public void fire(Set<String> keys) {
		for(Shortcut sh:shortcuts) {
			if(sh.isEqual(keys))handler.run();
		}
	}
	/**
	 * @return the shortcuts
	 */
	public Set<Shortcut> getShortcuts() {
		return shortcuts;
	}
	/**
	 * @param shortcuts the shortcuts to set
	 */
	public void setShortcuts(Set<Shortcut> shortcuts) {
		this.shortcuts = shortcuts;
	}
	/**
	 * @return the handler
	 */
	public Runnable getHandler() {
		return handler;
	}
	/**
	 * @param handler the handler to set
	 */
	public void setHandler(Runnable handler) {
		this.handler = handler;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
