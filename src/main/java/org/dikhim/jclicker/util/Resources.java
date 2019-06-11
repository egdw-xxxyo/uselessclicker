package org.dikhim.jclicker.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class Resources {
    public static String getSource(String uri) {
        return getSource(uri, "UTF-8");
    }

    public static String getSource(String uri, String encoding) {
        String source = "";
        try {
            source = IOUtils.toString(Resources.class.getResourceAsStream(uri), encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source;
    }

    public static String getFullURL(String uri) {
        String url = "";
        try {
            url = String.valueOf(Resources.class.getResource(uri).toURI().toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static InputStream getInputStream(String uri) {
        return Resources.class.getResourceAsStream(uri);
    }
}
