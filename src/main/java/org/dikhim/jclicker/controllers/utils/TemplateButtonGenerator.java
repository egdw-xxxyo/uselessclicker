package org.dikhim.jclicker.controllers.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.dikhim.jclicker.jsengine.objects.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.util.SourcePropertyFile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TemplateButtonGenerator {
    private boolean isBuilt;
    
    private int lineSize;
    private SourcePropertyFile properties;
    private KeyboardObjectCodeGenerator keyboardObjectCodeGenerator;
    private List<String> styleClasses = new ArrayList<>();
    
    private Consumer<MouseEvent> onMouseEntered;
    private Consumer<MouseEvent>  onMouseExited;

    public TemplateButtonGenerator() {
    }
    
    public TemplateButtonGenerator addStyleClass(String clazz) {
        styleClasses.add(clazz);
        isBuilt = false;
        return this;
    }

    public TemplateButtonGenerator setOnMouseEntered(Consumer<MouseEvent> consumer) {
        this.onMouseEntered = consumer;
        isBuilt = false;
        return this;
    }
    
    public TemplateButtonGenerator setOnMouseExited(Consumer<MouseEvent> consumer) {
        this.onMouseExited = consumer;
        isBuilt = false;
        return this;
    }

    public TemplateButtonGenerator setLineSize(int lineSize) {
        this.lineSize = lineSize;
        isBuilt = false;
        return this;
    }

    public TemplateButtonGenerator setProperties(SourcePropertyFile properties) {
        this.properties = properties;
        isBuilt = false;
        return this;
    }
    
    public TemplateButtonGenerator build(){
        keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator(lineSize);
        isBuilt = true;
        return this;
    }


    List<Button> getButtonListForKeyboardObject() {
        List<Button> buttons = new ArrayList<>();
        Button button = new Button();
        return buttons;
    }

}
