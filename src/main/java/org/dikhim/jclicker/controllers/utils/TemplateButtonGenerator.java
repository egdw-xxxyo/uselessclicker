package org.dikhim.jclicker.controllers.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.dikhim.jclicker.jsengine.objects.generators.KeyboardObjectCodeGenerator;
import org.dikhim.jclicker.util.SourcePropertyFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TemplateButtonGenerator {
    private static Logger LOGGER = LoggerFactory.getLogger(TemplateButtonGenerator.class);

    private boolean isBuilt;

    private int lineSize;
    private SourcePropertyFile properties;
    private KeyboardObjectCodeGenerator keyboardObjectCodeGenerator;
    private List<String> styleClasses = new ArrayList<>();

    private Consumer<MouseEvent> onMouseEntered;
    private Consumer<MouseEvent> onMouseExited;
    private Consumer<ActionEvent> onAction;

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

    public TemplateButtonGenerator setOnAction(Consumer<ActionEvent> onAction) {
        this.onAction = onAction;
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

    public TemplateButtonGenerator build() {
        keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator(lineSize);
        isBuilt = true;
        return this;
    }

    public List<Button> getButtonListForKeyboardObject() {
        return createButtons("key", keyboardObjectCodeGenerator.getMethodNames());
    }
    
    private List<Button> createButtons(String objectName, List<String> methodNames) {
        List<Button> buttons = new ArrayList<>();
        methodNames.forEach(name -> {
            String id = objectName+"_" + name;
            Button button = new Button();

            String text = properties.get(id);
            if (text.isEmpty()) text = name;
            String hint = properties.get(id + "_hint");
            if (hint.isEmpty()) hint = "insert " + id;
            String template = properties.get(id + "_template");
            if (template.isEmpty()) template = hint.split("[\\r\\n]+")[0]+"\n";

            button.setText(text);
            button.setUserData(new String[]{hint, template});
            button.setOnMouseEntered(onMouseEntered::accept);
            button.setOnMouseExited(onMouseExited::accept);
            button.setOnAction(onAction::accept);

            styleClasses.forEach(style -> button.getStyleClass().add(style));
            buttons.add(button);
        });
        return buttons;
    }

}
