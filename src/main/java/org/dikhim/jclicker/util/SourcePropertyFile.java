package org.dikhim.jclicker.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class SourcePropertyFile {
	String source;
	public SourcePropertyFile(File file) {
		try {
			this.source = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cannot read source prop file");
		}
	}

	public String get(String propertyName) {
		int begin = source.indexOf("$" + propertyName + ":\n");
		if (begin == -1)
			return "";
		begin += propertyName.length() + 3;
		int end = source.indexOf("\n$", begin);
		if (end == -1)
			end = source.length();
		return source.substring(begin, end);
	}

}
