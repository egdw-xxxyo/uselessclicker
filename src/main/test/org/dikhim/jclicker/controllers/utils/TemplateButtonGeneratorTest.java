package org.dikhim.jclicker.controllers.utils;

import javafx.scene.control.Button;
import org.dikhim.jclicker.util.SourcePropertyFile;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class TemplateButtonGeneratorTest {

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
}