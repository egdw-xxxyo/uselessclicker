package org.dikhim.jclicker.util;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

public class FormattedPropertiesTest {
    @Test
    public void test() throws IOException {
        Properties properties = new FormattedProperties();
        properties.load(new BufferedReader(new StringReader("prop=firstLine\\n   secondLine")));
        Assert.assertEquals("firstLine\n   secondLine", properties.getProperty("prop"));
    }

}