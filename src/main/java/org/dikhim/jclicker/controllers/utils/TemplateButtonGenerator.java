package org.dikhim.jclicker.controllers.utils;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.dikhim.jclicker.jsengine.clickauto.generators.*;
import org.dikhim.jclicker.util.Out;
import org.dikhim.jclicker.util.SourcePropertyFile;

import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateButtonGenerator {

    private int lineSize;
    private Properties properties;
    private KeyboardCodeGenerator keyboardCodeGenerator;
    private MouseCodeGenerator mouseCodeGenerator;
    private SystemCodeGenerator systemCodeGenerator;
    private ScreenCodeGenerator screenCodeGenerator;
    private CombinedCodeGenerator combinedCodeGenerator;
    private ClipboardCodeGenerator clipboardCodeGenerator;
    private CreateCodeGenerator createCodeGenerator;
    
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

    public TemplateButtonGenerator setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public TemplateButtonGenerator build() {
        
        keyboardCodeGenerator = new KeyboardCodeGenerator(lineSize);
        mouseCodeGenerator = new MouseCodeGenerator(lineSize);
        systemCodeGenerator = new SystemCodeGenerator(lineSize);
        screenCodeGenerator = new ScreenCodeGenerator(lineSize);
        clipboardCodeGenerator = new ClipboardCodeGenerator(lineSize);
        combinedCodeGenerator = new CombinedCodeGenerator(lineSize);
        createCodeGenerator = new CreateCodeGenerator(lineSize);
        return this;
    }

    public void addButtonsForKeyboardObject(VBox buttonPanel) {
        buttonPanel.getChildren().addAll(createButtons("key", keyboardCodeGenerator.getMethodNames()));
    }
    
    public List<Button> getButtonListForKeyboardObject() {
        return createButtons("key", keyboardCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForMouseObject() {
        return createButtons("mouse", mouseCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForSystemObject() {
        return createButtons("system", systemCodeGenerator.getMethodNames());
    }
    
    public List<Button> getButtonListForScreenObject() {
        return createButtons("screen", screenCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForClipboardObject() {
        return createButtons("clipboard", clipboardCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForCombinedObject() {
        return createButtons("combined", combinedCodeGenerator.getMethodNames());
    }
    public List<Button> getButtonListForCreateObject() {
        return createButtons("create", createCodeGenerator.getMethodNames());
    }

    public List<Button> getButtonListForLanguage() {
        return createButtons("language", new ArrayList<>(getButtonNamesFromPropertyFile("language")));
    }
    
    
    
    private List<Button> createButtons(String objectName, List<String> methodNames) {
        List<Button> buttons = new ArrayList<>();
        methodNames.forEach(methodName -> {
            String id = objectName+"." + methodName;
            Button button = new Button();

            String text = properties.getProperty(id + ".name");
            if (text == null) text = methodName;
            String hint = properties.getProperty(id + ".hint");
            if (hint == null) {
                Out.println("no hint for :" + id);
                hint = id;
            }
            String template = properties.getProperty(id + ".code");
            if (template == null) {
                Out.println("no code for :" + id);

                template = hint.split("[\\r\\n]+")[0];
            }
            template += "\n";

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

    private Set<String> getButtonNamesFromPropertyFile(String objectName) {
        Set<String> buttonNames = new HashSet<>();
        Pattern pattern = Pattern.compile(objectName + ".([a-zA-Z]+).");
        Set<Object> propertyNames = properties.keySet();
        propertyNames.forEach(propName->{
            Matcher matcher = pattern.matcher((CharSequence) propName);
            while (matcher.find()) {
                String buttonName = matcher.group(1);
                buttonNames.add(buttonName);
            }

        });
        return buttonNames;
}

}
