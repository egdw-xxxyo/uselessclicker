package org.dikhim.jclicker.actions;

import org.dikhim.jclicker.actions.events.KeyboardEvent;

import java.util.Set;
import java.util.function.Consumer;

public interface KeyboardListener {
	String getName();
	
	void setKeys(String keys);
	String getKeys();
	
	void setHandler(Consumer<KeyboardEvent> handler);
	Consumer<KeyboardEvent> getHandler();
	
	void fire(KeyboardEvent keyboardEvent);
	
}
