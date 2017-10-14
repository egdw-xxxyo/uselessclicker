package org.dikhim.jclicker.events;

import java.util.Set;

public interface ShortcutHandler {
	public void setName(String name);
	public String getName();
	
	public void setShortcuts(Set<Shortcut> shortcuts);
	public Set<Shortcut> getShortcuts();
	
	public void setHandler(Runnable handler);
	public Runnable getHandler();
	
	public void addShortcut(String shortcut);
	public void fire(Set<String> keys);
	
}
