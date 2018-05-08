package org.dikhim.jclicker.util.output;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Out {
	private static CustomOutput output;
	static {
		output = new SystemOutput();
	}

	public static void print(String text) {
		output.print(text);
	}
	public static void println(String text) {
		output.println(text);
	}
	
	public static String getString() {
		return output.getStringProperty().toString();
	}
	public static StringProperty outProperty() {
		return output.getStringProperty();
	}
	public static void clear() {
		output.clear();
	}
	public static void setOutput(CustomOutput output){
		Out.output = output;
	}
}
