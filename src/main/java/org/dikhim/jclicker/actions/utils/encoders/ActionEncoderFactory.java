package org.dikhim.jclicker.actions.utils.encoders;

import java.util.ArrayList;
import java.util.List;

public class ActionEncoderFactory {
    private static List<String> listOfEncodings = new ArrayList<>();
    
    static {
        listOfEncodings.add("unicode");
        listOfEncodings.add("base64");
    }
    
    public static ActionEncoder get(String encoding) {
        if ("unicode".equalsIgnoreCase(encoding)) {
            return new UnicodeActionEncoder();
        } else if ("base64".equalsIgnoreCase(encoding)) {
            return new Base64ActionEncoder();
        } 
        return null;
    }

    public static List<String> getListOfEncodings() {
        return listOfEncodings;
    }
    
}
