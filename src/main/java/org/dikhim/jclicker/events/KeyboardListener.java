package org.dikhim.jclicker.events;

import java.util.Set;
import java.util.function.Consumer;

public interface KeyboardListener {
	public void setName(String name);
	public String getName();
	
	public void setShortcuts(Set<Shortcut> shortcuts);
	public Set<Shortcut> getShortcuts();
	
	public void setHandler(Consumer<KeyboardEvent>  handler);
	public Consumer<KeyboardEvent> getHandler();
	
	public void addShortcut(String shortcut);
	public void fire(KeyboardEvent keyboardEvent);
	
}
