package org.dikhim.jclicker.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OutStream {
	private static StringProperty property = new SimpleStringProperty("");

	public static void print(String s) {
		if(property.get().length()>10000) {

			property.set(property.get().substring(500) + s);
		}else {
			property.set(property.get() + s);
		}
	}
	public static void println(String s) {
		if(property.get().length()>10000) {

			property.set(property.get().substring(500) + s + "\n");
		}else {
			property.set(property.get() + s + "\n");
		}
	}
	
	public static String getString() {
		return property.get();
	}
	public static StringProperty getProperty() {
		return property;
	}
}
