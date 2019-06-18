package org.dikhim.jclicker.jsengine.clickauto.generators;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewCodeGeneratorTest {

    @Test
    public void forMethod() {
        SimpleCodeGenerator codeGenerator = new SimpleCodeGenerator("object", new Object() {
            public String testMethod(boolean p1, int p2, long p3, float p4, double p5, String p6) {
                return p1 + ", " + p2 + ", " + p3 + ", " + p4 + ", " + p5 + ", " + p6;
            }
        }.getClass());

        assertEquals("object.testMethod(true,1,2,3.0,4.0,'TEST');\n", 
                codeGenerator.forMethod("testMethod", true, 1, 2, 3.0, 4.0, "TEST"));
    }
}