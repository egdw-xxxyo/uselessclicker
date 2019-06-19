package org.dikhim.jclicker.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.dikhim.jclicker.util.Out;

public class Script {
    private File file;
    private StringProperty name = new SimpleStringProperty("");
    private StringProperty code = new SimpleStringProperty("");

    public Script() {
        newScriptFile();
    }

    void newScriptFile() {
        this.file = null;
        this.name.set("newFile.js");
        this.code.set("");
    }

    void openScriptFile(File file) {
        if (file != null) {
            try {
                String code = FileUtils.readFileToString(file, "UTF-8");
                this.code.set(code);
                this.file = file;
                this.name.set(file.getName());
            } catch (IOException e) {
                Out.println(e.getMessage());
            }
        }
    }

    void saveScriptFile() {
        if (file != null) {
            saveScriptFileAs(file);
        }
    }

    void saveScriptFileAs(File file) {
        try {
            String code = this.code.get();
            FileUtils.writeStringToFile(file, code, "UTF-8");
            this.file = file;
            this.name.set(file.getName());
        } catch (IOException e) {
            Out.println(e.getMessage());
        }
    }


    /**
     * @return the code
     */
    public StringProperty codeProperty() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(StringProperty code) {
        this.code = code;
    }


    public String getCode() {
        return code.get();
    }

    public String getName() {
        return name.get();
    }

    StringProperty nameProperty() {
        return name;
    }

    public boolean isOpened() {
        return file != null;
    }
}
