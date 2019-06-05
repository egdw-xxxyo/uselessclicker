package org.dikhim.jclicker;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestPropertyFile {
    @Test
    public void test() throws IOException {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("i18n/codeSamples_en.properties");
        properties.load(stream);
        System.out.println(properties.getProperty("btnInsertKeyName"));
    }
}
