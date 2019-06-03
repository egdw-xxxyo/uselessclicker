package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.dikhim.jclicker.jsengine.clickauto.objects.KeyboardObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;

public class KeyboardObjectCodeGeneratorTest {
    private static KeyboardObjectCodeGenerator generator;
    @BeforeClass
    public static void init() {
        generator = new KeyboardObjectCodeGenerator();
    }

    @Test
    public void getMethodNames() {
        Method[] methodArray = KeyboardObject.class.getDeclaredMethods();
        List<String> methodList = generator.getMethodNames();
        assertEquals(methodArray.length, methodList.size());
        
    }
}