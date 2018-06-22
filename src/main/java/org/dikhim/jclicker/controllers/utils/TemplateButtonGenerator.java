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

    public TemplateButtonGenerator build() {
        keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator(lineSize);
        isBuilt = true;
        return this;
    }

    public List<Button> getButtonListForKeyboardObject() {
        //TODO
        List<Button> buttons = new ArrayList<>();
        keyboardObjectCodeGenerator.getMethodNames().forEach(name -> {
            String id = "key_" + name;
            Button button = new Button();

            String text = properties.get(id);
            String hint = properties.get(id + "_hint");
            String[] s = hint.split("[\\r\\n]+");
            String template = hint.split("\\\\r?\\\\n", 2)[0];

            button.setText(text);
            button.setUserData(new String[]{hint,template});
            button.setOnMouseEntered(onMouseEntered::accept);
            button.setOnMouseExited(onMouseExited::accept);

            styleClasses.forEach(style -> button.getStyleClass().add(style));
            buttons.add(button);
        });
            return buttons;
        }

    }
