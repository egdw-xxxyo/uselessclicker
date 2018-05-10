package org.dikhim.jclicker.actions;

import org.dikhim.jclicker.actions.events.KeyboardEvent;

import java.util.Set;
import java.util.function.Consumer;

public interface KeyboardListener {
	public void setName(String name);
	public String getName();
	
	public void setStringShortcuts(Set<StringShortcut> stringShortcuts);
	public Set<StringShortcut> getStringShortcuts();
	
	public void setHandler(Consumer<KeyboardEvent>  handler);
	public Consumer<KeyboardEvent> getHandler();
	
	public void addShortcut(String shortcut);
	public void fire(KeyboardEvent keyboardEvent);
	
}
