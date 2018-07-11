package org.dikhim.jclicker.controllers.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.dikhim.jclicker.jsengine.objects.generators.*;
import org.dikhim.jclicker.util.SourcePropertyFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateButtonGenerator {

    private int lineSize;
    private SourcePropertyFile properties;
    private KeyboardObjectCodeGenerator keyboardObjectCodeGenerator;
    private MouseObjectCodeGenerator mouseObjectCodeGenerator;
    private SystemObjectCodeGenerator systemObjectCodeGenerator;
    private CombinedObjectCodeGenerator combinedObjectCodeGenerator;
    private ClipboardObjectCodeGenerator clipboardObjectCodeGenerator;
    private CreateObjectCodeGenerator createObjectCodeGenerator;
    private List<String> styleClasses = new ArrayList<>();

    private Consumer<MouseEvent> onMouseEntered;
    private Consumer<MouseEvent> onMouseExited;
    private Consumer<ActionEvent> onAction;

    public TemplateButtonGenerator() {
    }

    public TemplateButtonGenerator addStyleClass(String clazz) {
        styleClasses.add(clazz);
        return this;
    }

    public TemplateButtonGenerator setOnMouseEntered(Consumer<MouseEvent> consumer) {
        this.onMouseEntered = consumer;
        return this;
    }

    public TemplateButtonGenerator setOnMouseExited(Consumer<MouseEvent> consumer) {
        this.onMouseExited = consumer;
        return this;
    }

    public TemplateButtonGenerator setOnAction(Consumer<ActionEvent> onAction) {
        this.onAction = onAction;
        return this;
    }

    public TemplateButtonGenerator setLineSize(int lineSize) {
        this.lineSize = lineSize;
        return this;
    }

    public TemplateButtonGenerator setProperties(SourcePropertyFile properties) {
        this.properties = properties;
        return this;
    }

    public TemplateButtonGenerator build() {
        keyboardObjectCodeGenerator = new KeyboardObjectCodeGenerator(lineSize);
        mouseObjectCodeGenerator = new MouseObjectCodeGenerator(lineSize);
        systemObjectCodeGenerator = new SystemObjectCodeGenerator(lineSize);
        clipboardObjectCodeGenerator = new ClipboardObjectCodeGenerator(lineSize);
        combinedObjectCodeGenerator = new CombinedObjectCodeGenerator(lineSize);
        createObjectCodeGenerator = new CreateObjectCodeGenerator(lineSize);
        return this;
    }

    public List<Button> getButtonListForKeyboardObject() {
        return createButtons("key", keyboardObjectCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForMouseObject() {
        return createButtons("mouse", mouseObjectCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForSystemObject() {
        return createButtons("system", systemObjectCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForClipboardObject() {
        return createButtons("clipboard", clipboardObjectCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForCombinedObject() {
        return createButtons("combined", combinedObjectCodeGenerator.getMethodNames());
    }
    public List<Button> getButtonListForCreateObject() {
        return createButtons("create", createObjectCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForLanguage() {
        return createButtons("language", getButtonNamesFromPropertyFile("language"));
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
        buttons.sort(Comparator.comparing(Button::getText));
        return buttons;
    }

    private List<String> getButtonNamesFromPropertyFile(String objectName) {
        List<String> buttonNames = new ArrayList<>();
        Pattern pattern = Pattern.compile(objectName + "_([a-zA-Z]*)_hint");
        Set<String> propertyNames = properties.getKetSet();
        propertyNames.forEach(propName->{
            Matcher matcher = pattern.matcher(propName);
            while (matcher.find()) {
                String buttonName = matcher.group(1);
                buttonNames.add(buttonName);
            }

        });
        return buttonNames;
}

}
