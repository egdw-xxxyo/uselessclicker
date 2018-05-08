package org.dikhim.jclicker.actions;

import java.util.HashSet;
import java.util.Set;

public class Shortcut {
	private Set<String> keys;

	/**
	 *
	 * @param shortcut
	 *            Key shortcut like 'CTRL ALT T'
	 */
	public Shortcut(Set<String> shortcut) {
		keys = shortcut;
	}
	public Shortcut(String shortcut) {
		String[] strKeys = shortcut.split(" ");
		keys = new HashSet<>();
		for (String s : strKeys) {
			if (!s.equals(""))
				keys.add(s);
		}

	}

	public boolean isEqual(Set<String> shortcut) {
		if (keys.equals(shortcut))
			return true;
		return false;
	}

	public boolean containsIn(Set<String> shortcut) {
		if(keys.isEmpty())return true;
		if (shortcut.containsAll(keys))
			return true;
		return false;
	}
	public Set<String> getKeys() {
		return keys;
	}

}