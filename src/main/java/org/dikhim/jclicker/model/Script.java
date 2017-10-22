package org.dikhim.jclicker.model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Script {
	private File scriptFile;
	private StringProperty script;
	public Script() {
		script = new SimpleStringProperty();

	}
	public Script(File file) {
		script = new SimpleStringProperty();
		this.scriptFile = file;
		try {
			this.script.set(FileUtils.readFileToString(file, Charset.defaultCharset()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @return the scriptFile
	 */
	public File getScriptFile() {
		return scriptFile;
	}
	/**
	 * @param scriptFile
	 *            the scriptFile to set
	 */
	public void setScriptFile(File scriptFile) {
		this.scriptFile = scriptFile;
	}
	/**
	 * @return the script
	 */
	public StringProperty getStringProperty() {
		return script;
	}
	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(StringProperty script) {
		this.script = script;
	}

}
