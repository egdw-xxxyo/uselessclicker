package org.dikhim.jclicker.controllers.utils;

import javafx.scene.control.Button;
import org.dikhim.jclicker.util.SourcePropertyFile;
import org.junit.Test;

import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class TemplateButtonGeneratorTest {
    private static String sourse = "$language_class_hint:\n" +
            "function Class (path) {\n" +
            "    this.path = path;\n" +
            "    this.echo = function(text){\n" +
            "        return text;\n" +
            "    };\n" +
            "}\n" +
            "The template for the class with the name 'Class'\n" +
            "  \n" +
            "To create an object use the code:\n" +
            "  var obj = new Class('MyClass');\n" +
            "  \n" +
            "To get the field from object:\n" +
            "  var out = obj.path;\n" +
            "  \n" +
            "To call a method from object:\n" +
            "  var out = obj.echo('someText');\n" +
            "\n" +
            "$language_functionEcho_hint:\n" +
            "function echo(text){\n" +
            "    return text;\n" +
            "}\n" +
            "The template of a function with a returning value\n" +
            "\n" +
            "$language_functionVoid_hint:\n" +
            "function foo(){\n" +
            "    \n" +
            "}\n" +
            "The template for an empty function\n" +
            "\n" +
            "$language_loopForPlus_hint:\n" +
            "for(i=0;i<10;i++){\n" +
            "    \n" +
            "}\n" +
            "The template for an increasing loop 'for'\n" +
            "\n" +
            "$language_loopForMinus_hint:\n" +
            "for(i=10;i>0;i--){\n" +
            "    \n" +
            "}\n" +
            "The template for a decreasing loop 'for'\n" +
            "\n" +
            "$language_loopWhile_hint:\n" +
            "while(true){\n" +
            "    \n" +
            "}\n" +
            "The template for a loop 'while'";

    @Test
    public void getButtonListForKeyboardObject() {
        SourcePropertyFile sourcePropertyFile = new SourcePropertyFile(new File(getClass().getResource("/i18n/codesamples_en.txt").getFile()));
        TemplateButtonGenerator templateButtonGenerator = new TemplateButtonGenerator()
                .setLineSize(80)
                .setProperties(sourcePropertyFile)
                .build();
        List<Button> list = templateButtonGenerator.getButtonListForKeyboardObject();
        assertFalse(list.isEmpty());
    }

    @Test
    public void getButtonNamesFromPropertyFile() {
        SourcePropertyFile sourcePropertyFile = new SourcePropertyFile();
        sourcePropertyFile.setSource(sourse);
        TemplateButtonGenerator templateButtonGenerator = new TemplateButtonGenerator()
                .setLineSize(80)
                .setProperties(sourcePropertyFile)
                .build();
        List<Button> list = templateButtonGenerator.getButtonListForLanguage();
        assertFalse(list.isEmpty());


    }
}