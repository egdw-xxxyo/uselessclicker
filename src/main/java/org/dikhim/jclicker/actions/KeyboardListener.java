package org.dikhim.jclicker.actions;

import org.dikhim.jclicker.actions.events.KeyboardEvent;

import java.util.Set;
import java.util.function.Consumer;

public interface KeyboardListener {
	public void setName(String name);
	public String getName();
	
	public void setStringShortcuts(Set<Shortcut> stringShortcuts);
	public Set<Shortcut> getStringShortcuts();
	
	public void setHandler(Consumer<KeyboardEvent>  handler);
	public Consumer<KeyboardEvent> getHandler();
	
	public void addShortcut(Shortcut shortcut);
	public void fire(KeyboardEvent keyboardEvent);
	
}
