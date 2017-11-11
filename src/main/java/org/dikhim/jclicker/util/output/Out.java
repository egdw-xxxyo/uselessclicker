package org.dikhim.jclicker.util.output;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Out {
	private static CustomOutput output;
	static {
		output = new SystemOutput();
	}

	private static StringProperty property = new SimpleStringProperty("");

	public static void print(String text) {
		output.print(text);
	}
	public static void println(String text) {
		output.println(text);
	}
	
	public static String getString() {
		return property.get();
	}
	public static StringProperty getProperty() {
		return property;
	}
	public static void clear() {
		property.set("");
	}
	public static void setOutput(CustomOutput output){
		Out.output = output;
	}
}
