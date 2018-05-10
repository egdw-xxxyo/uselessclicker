package org.dikhim.jclicker.actions;

import java.util.Set;

public class StringShortcut implements Shortcut {
	private Set<String> keys;
	
	StringShortcut(String shortcut) {
		keys = getKeySet(shortcut);
	}

	public boolean isEqual(Set<String> shortcut) {
		return keys.equals(shortcut);
	}

	public boolean containsIn(Set<String> shortcut) {
		if(keys.isEmpty())return true;
		return shortcut.containsAll(keys);
	}
	public Set<String> getKeys() {
		return keys;
	}

}