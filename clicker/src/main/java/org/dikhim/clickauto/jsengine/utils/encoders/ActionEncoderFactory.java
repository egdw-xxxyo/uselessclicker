package org.dikhim.clickauto.jsengine.utils.encoders;

import java.util.ArrayList;
import java.util.List;

public class ActionEncoderFactory {
    private static List<String> listOfEncodings = new ArrayList<>();
    
    static {
        listOfEncodings.add("unicode");
        listOfEncodings.add("base64");
        listOfEncodings.add("base64-zip");
    }
    
    public static ActionEncoder get(String encoding) {
        if ("unicode".equalsIgnoreCase(encoding)) {
            return new UnicodeActionEncoder();
        } else if ("base64".equalsIgnoreCase(encoding)) {
            return new Base64ActionEncoder();
        } else if ("base64-zip".equalsIgnoreCase(encoding)) {
            return new Base64ZipActionEncoder();
        } else if ("natural".equalsIgnoreCase(encoding)) {
            return new NaturalActionEncoder();
        } 
        return null;
    }

    public static List<String> getListOfEncodings() {
        return listOfEncodings;
    }
    
}
