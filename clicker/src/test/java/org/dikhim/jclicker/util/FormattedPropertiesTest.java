package org.dikhim.jclicker.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class FormattedPropertiesTest {
    @Test
    public void testFormattedProperetyFIle() throws IOException {
        Properties properties = new FormattedProperties();
        properties.load(new BufferedReader(new StringReader("prop=firstLine\\n   secondLine")));
        Assert.assertEquals("firstLine\n   secondLine", properties.getProperty("prop"));
    }
    
    @Test
    public void testDefaultPropertyFile() throws IOException {
        Properties properties = new Properties();
        properties.load(new BufferedReader(new StringReader("prop=firstLine\\n\\   secondLine")));
        System.out.println(properties.getProperty("prop"));
        Assert.assertEquals("firstLine\n   secondLine", properties.getProperty("prop"));
    }
}