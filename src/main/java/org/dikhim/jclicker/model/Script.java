package org.dikhim.jclicker.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.util.output.Out;

public class Script {
	private File file;
	private StringProperty stringProperty;
	private String name;
	public Script() {
		stringProperty = new SimpleStringProperty();
		name = "newFile.js";
	}

	public Script(String filePath){
		if(filePath.equals("")){
			stringProperty = new SimpleStringProperty();
			name = "newFile.js";
		}else{
			file = new File(filePath);
			name = file.getName();
			try {
				this.stringProperty.set(FileUtils.readFileToString(file, Charset.defaultCharset()));
			} catch (IOException e) {
				Out.println(e.getMessage());
			}
		}
	}
	public Script(File file) {
		stringProperty = new SimpleStringProperty();
		this.file = file;
		name = file.getName();
		try {
			this.stringProperty.set(FileUtils.readFileToString(file, Charset.defaultCharset()));
		} catch (IOException e) {
			Out.println(e.getMessage());
		}
	}
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
		name = file.getName();
	}
	/**
	 * @return the stringProperty
	 */
	public StringProperty getStringProperty() {
		return stringProperty;
	}
	/**
	 * @param stringProperty
	 *            the stringProperty to set
	 */
	public void setStringProperty(StringProperty stringProperty) {
		this.stringProperty = stringProperty;
	}
	
	public String getName() {
		return name;
	}

	/**
	 * Loads script without crating new StringProperty
	 * @param path of a file
	 */
	public void loadScript(String path){
		Out.println("Open file:" +path);
		if(path.equals("")){
			file = null;
			name = "newFile.js";
			stringProperty.set("");
		}
		File oldFile = file;
		String oldName = name;
		String oldText = stringProperty.getValue();
		try {
			file = new File(path);
			name = file.getName();
			stringProperty.set(FileUtils.readFileToString(file, Charset.defaultCharset()));
		} catch (NullPointerException|IOException e) {
			Out.println(e.getMessage());
			file = oldFile;
			name = oldName;
			stringProperty.set(oldText);
		}
	}

}
